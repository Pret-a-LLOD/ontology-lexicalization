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


my $text_ID = "QALD5";

	open(my $fh, #"<:encoding(UTF-8)", 
		"/opt/massive_correlation/data/EnglishDBpediaAbstracts/parsed/QALD5.txt.json");

	my $json_string = q{};
	while(defined(my $line = <$fh>)){
		$json_string .= $line;
	}
	close $fh;
	my $data = decode_json($json_string);

	my $sentence_ID = 0;
	foreach my $sentence (@{$data->{sentences}}){
		my $sentence_text = q{};
		my $tokens = [];
		my $postags = [];

		foreach my $token (@{$sentence->{tokens}}){
			$sentence_text .= $token->{originalText} . $token->{after};
			push(@{$tokens}, [$token->{originalText}, $token->{after}, $token->{word}]);
			push(@{$postags}, $token->{pos});
		}	

		$A->{"$text_ID/$sentence_ID"} = {
			sentence_text => $sentence_text,
			tokens => $tokens,
			postags => $postags,
			annotations => [],
			parse => $sentence->{parse},
		};

		$sentence_ID++;
	}


store $A, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/qald5-1.dat";

