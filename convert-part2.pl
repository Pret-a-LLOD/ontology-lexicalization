#!/usr/bin/perl -w
use strict;
use YAML qw(LoadFile DumpFile Dump);
#use Data::Dumper;
use Storable;
use URL::Encode qw(url_encode_utf8);
use JSON::Parse 'parse_json';
use JSON qw(decode_json);
binmode STDOUT, ":utf8"; 

require "/opt/massive_correlation/code/EnglishDBpediaAbstracts/clean_text.pl";

# read R1.yml, read parsed files, create structure dscribed in paper. maybe, 100 sentences per file.

my $A = {};

die if not defined $ARGV[0];
my $FILENR = $ARGV[0];

#my $D = LoadFile("/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}-0.yml");

print "<load $FILENR ...\n";
#open my $in, '<', "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}-0.yml" or die $!;
#my $D;
#{
#   local $/;    # slurp mode
#    $D = eval <$in>;
#}
#close $in;

my $D = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}-0.dat");

print "... load $FILENR>\n";

#print Dump { D => $D };

my $cnt = 0;
foreach my $text_ID (sort keys %{$D}){
	#next if $text_ID ne "Dinosaur/abstract#offset_0_3682";
	print "text_ID: $text_ID\n";
	#last if ++$cnt > 100; # TODO remove
	my $filename = url_encode_utf8($text_ID);
	if(not -s "/opt/massive_correlation/data/EnglishDBpediaAbstracts/parsed/$filename.json"){
		print "Text not parsed: $text_ID\n"; #<STDIN>;
		next;
	}
	
	open(my $fh, #"<:encoding(UTF-8)", 
		"/opt/massive_correlation/data/EnglishDBpediaAbstracts/parsed/$filename.json");

	my $json_string = q{};
	while(defined(my $line = <$fh>)){
		$json_string .= $line;
	}
	close $fh;
	my $data = decode_json($json_string);

	#DumpFile("test.yml", $data);

	#print Dump { data => $data }; <STDIN>;


	my $annotationbeginIndex_to_annotationID = {};
	my $annotationbeginIndex_to_annotation = {};
	my $annotationendIndex_to_annotationID = {};

	
	foreach my $annotation_ID (keys %{$D->{$text_ID}->{annotations}}){
		my $annotation = $D->{$text_ID}->{annotations}->{$annotation_ID};
		$annotation->{anchor} = &clean_text($annotation->{anchor});
		$annotationbeginIndex_to_annotationID->{$annotation->{beginIndex}} = $annotation_ID; # if $annotation->{beginIndex} < 500;
		$annotationbeginIndex_to_annotation->{$annotation->{beginIndex}} = $annotation->{anchor};# if $annotation->{beginIndex} < 500;
		$annotationendIndex_to_annotationID->{$annotation->{endIndex}} = $annotation_ID;
	}

	#my $number_of_tokens_in_previous_sentences = 0;


	my $sentence_ID = 0;
	foreach my $sentence (@{$data->{sentences}}){
		my $sentence_text = q{};
		my $tokens = [];
		my $postags = [];
		my $annotations = [];

		my $characterOffsetBegin_to_tokenID = {};
		my $characterOffsetBegin_to_token = {};
		my $characterOffsetEnd_to_tokenID = {};

		my $sentencewise_tokenID = 0;

		foreach my $token (@{$sentence->{tokens}}){

			$sentence_text .= $token->{originalText} . $token->{after};

			$characterOffsetBegin_to_tokenID->{$token->{characterOffsetBegin}} = $sentencewise_tokenID;
			$characterOffsetEnd_to_tokenID->{$token->{characterOffsetEnd}} = $sentencewise_tokenID;
			$characterOffsetBegin_to_token->{ sprintf("%03d", $token->{characterOffsetBegin})} = $token->{originalText};

			push(@{$tokens}, [$token->{originalText}, $token->{after}, $token->{word}]);
			push(@{$postags}, $token->{pos});

			$sentencewise_tokenID++;
			
		}	

		#print Dump {
		#	characterOffsetBegin_to_tokenID => $characterOffsetBegin_to_tokenID,
		#	characterOffsetEnd_to_tokenID => $characterOffsetEnd_to_tokenID,
		#	characterOffsetBegin_to_token => $characterOffsetBegin_to_token,
		#	annotationbeginIndex_to_annotationID => $annotationbeginIndex_to_annotationID,
		#	annotationbeginIndex_to_annotation => $annotationbeginIndex_to_annotation,
		#}; <STDIN>;


		$sentencewise_tokenID = 0;

		my $additional_offset_0 = 0;
		my $additional_offset_1 = 0;



		foreach my $annotation_ID (keys %{$D->{$text_ID}->{annotations}}){
			my $annotation = $D->{$text_ID}->{annotations}->{$annotation_ID};
			if(
				exists $characterOffsetBegin_to_tokenID->{$annotation->{beginIndex}} and
				exists $characterOffsetEnd_to_tokenID->{$annotation->{endIndex}}
			){
				my $first_token_ID = $characterOffsetBegin_to_tokenID->{$annotation->{beginIndex}};
				my $last_token_ID = $characterOffsetEnd_to_tokenID->{$annotation->{endIndex}};

				#print "Annotation <" . $annotation->{anchor} . ">: $first_token_ID - $last_token_ID\n"; <STDIN>;
				push(@{$annotations}, [
					$first_token_ID,
					$last_token_ID,
					$annotation->{KBID},
					$annotation->{anchor},
				]);

			}	
				

		}		

		#print Dump {
		#	ID => "$text_ID/$sentence_ID",
		#	sentence_text => $sentence_text,
		#	#tokens => $tokens,
		#	#postags => $postags,
		#	annotations => $annotations,
		#}; <STDIN>;

		$A->{"$text_ID/$sentence_ID"} = {
			sentence_text => $sentence_text,
			tokens => $tokens,
			postags => $postags,
			annotations => $annotations,
			parse => $sentence->{parse},
		};

		$sentence_ID++;
	}

	#print Dump { A => $A };
	#DumpFile("/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}-1.yml", $A);
	#store $A, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}-1.yml";

}

store $A, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}-1.dat";

