#!/usr/bin/perl -w
# vim:si:ts=4:sw=4
use strict;
use YAML qw(Dump DumpFile);
#use Data::Dumper;
use Storable;
use URL::Encode qw(url_encode_utf8);
use Cwd qw(cwd);
use utf8;
require "/opt/massive_correlation/code/EnglishDBpediaAbstracts/clean_text.pl";

my $D = {
	tokenID_to_textID => {},
	token_IDs => {},
	text_IDs => {},
	texts => {},
	annotations => {},
};

my $limit = 0; # 0 means no limit


# collect all IDs of tokens that are annotated with a KB concept

die if not defined $ARGV[0];
my $FILENR = $ARGV[0];

open (DAT, "</opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}.nt") or die "file not found";
my $cnt1 = 0;
while(defined(my $line=<DAT>)){ last if ++$cnt1 > $limit and $limit;
	if($line =~ m/(.*)\s<http:\/\/www.w3.org\/2005\/11\/its\/rdf#taIdentRef>\s<(.*)>/){
		$D->{token_IDs}->{&shorten($1)} = 1;
		$D->{annotations}->{&shorten($1)}->{KBID} = $2;
	}

}
close DAT;

# collect relation between tokenIDs and textIDs and further annotations
#open (DAT, "<:encoding(UTF-8)abstracts_en0.nt");
open my $fh1, '<:encoding(UTF-8)', "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}.nt";

my $cnt2 = 0;
while(defined(my $line=<$fh1>)){ last if ++$cnt2 > $limit and $limit;
	if($line =~ m/(.*)\s<http:\/\/persistence.uni-leipzig.org\/nlp2rdf\/ontologies\/nif-core#referenceContext> (.*) \./){
		if(exists $D->{token_IDs}->{&shorten($1)}){
			$D->{tokenID_to_textID}->{&shorten($1)}->{&shorten($2)} = 1;
			$D->{annotations}->{&shorten($1)}->{text_ID} = &shorten($2);
			$D->{text_IDs}->{&shorten($2)} = 1;
		}
	} elsif($line =~ m/(.*)\s<http:\/\/persistence.uni-leipzig.org\/nlp2rdf\/ontologies\/nif-core#anchorOf> "(.*)"/) {
		if(exists $D->{token_IDs}->{&shorten($1)}){
			$D->{annotations}->{&shorten($1)}->{anchor} = $2;
		}
	} elsif($line =~ m/(.*)\s<http:\/\/persistence.uni-leipzig.org\/nlp2rdf\/ontologies\/nif-core#beginIndex> "(.*)"/) {
		if(exists $D->{token_IDs}->{&shorten($1)}){
			$D->{annotations}->{&shorten($1)}->{beginIndex} = $2;
		}
	} elsif($line =~ m/(.*)\s<http:\/\/persistence.uni-leipzig.org\/nlp2rdf\/ontologies\/nif-core#endIndex> "(.*)"/){
		if(exists $D->{token_IDs}->{&shorten($1)}){
			$D->{annotations}->{&shorten($1)}->{endIndex} = $2;
		}
	}
}
close $fh1;


# collect texts and tokens

open my $fh2, '<:encoding(UTF-8)', "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}.nt";
my $cnt3 = 0;
while(defined(my $line=<$fh2>)){ last if ++$cnt3 > $limit and $limit;
	#next if $line !~ m/inosaur/;
	if($line =~ m/(.*)\s<http:\/\/persistence.uni-leipzig.org\/nlp2rdf\/ontologies\/nif-core#isString> "(.*)"/){
		if(exists $D->{text_IDs}->{&shorten($1)}){
			$D->{texts}->{&shorten($1)} = &clean_text($2);
		}
	}
}
close $fh2;

my @filelist = ();
foreach my $text_ID (keys %{$D->{texts}}){
	my $filename = url_encode_utf8($text_ID);
	if(not -s "/opt/massive_correlation/data/EnglishDBpediaAbstracts/parsed/$filename.json"){

		#print "File does not exist: </opt/massive_correlation/data/EnglishDBpediaAbstracts/parsed/$filename.json>\n";

		#print "Write to </opt/massive_correlation/code/stanford-corenlp-full-2018-10-05/$filename.txt>\n";

		open(
			my $fh, 
			'>',
			#'>:encoding(UTF-8)',
			"/opt/massive_correlation/code/stanford-corenlp-full-2018-10-05/$filename.txt"
		) or next; #die $!;
    		print $fh $D->{texts}->{$text_ID};
    		close($fh);

		#print "stanford-corenlp-full-2018-10-05/$filename.txt\n";

		#open(DAT,">stanford-corenlp-full-2018-10-05/$filename.txt"); ########3 utf8
		#print DAT $D->{texts}->{$text_ID};
		#close DAT;

		push(@filelist, $filename);
	} else {
		#print "File exists: </opt/massive_correlation/data/EnglishDBpediaAbstracts/parsed/$filename.json>\n";
	}
	#print "<STDIN>"; <STDIN>;
}

print Dump { filelist => \@filelist }; print "<filelist>\n"; #<STDIN>;

my $R = {};
foreach my $text_ID (keys %{$D->{texts}}){
	$R->{$text_ID} = { original_text => $D->{texts}->{$text_ID}, annotations => {} };
}
foreach my $token_ID (keys %{$D->{annotations}}){
	$R->{$D->{annotations}->{$token_ID}->{text_ID}}->{annotations}->{$token_ID} = {
		KBID => $D->{annotations}->{$token_ID}->{KBID},
		anchor => $D->{annotations}->{$token_ID}->{anchor},
		beginIndex => $D->{annotations}->{$token_ID}->{beginIndex},
		endIndex => $D->{annotations}->{$token_ID}->{endIndex},
	};
}


#print Dump { R => $R }; <STDIN>;

#DumpFile("/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}-0.yml", $R);

print "<store abstracts_en${FILENR}-0.dat ...\n";
store $R, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en${FILENR}-0.dat";
print "store>\n";

if(scalar @filelist){
	open(OUT,">/opt/massive_correlation/code/stanford-corenlp-full-2018-10-05/filelist-$FILENR.txt");
	print OUT "$_.txt\n" foreach @filelist;
	close OUT;

	my $cwd = cwd;

	chdir("/opt/massive_correlation/code/stanford-corenlp-full-2018-10-05") or print "could not chdir: $!";
	my $CMD1 = "java -cp \"*\" edu.stanford.nlp.pipeline.StanfordCoreNLP -quiet -annotators tokenize,ssplit,pos,parse,depparse -fileList \"filelist-$FILENR.txt\" -outputExtension .json -outputFormat json -output.columns word > /dev/null";
	`$CMD1`;
	chdir($cwd);

	foreach my $file (@filelist){
		if(not -e "/opt/massive_correlation/code/stanford-corenlp-full-2018-10-05/$file.txt.json"){
			print "<<</opt/massive_correlation/code/stanford-corenlp-full-2018-10-05/$file.txt.json>>> missing\n"; <STDIN>;
		}

		my $CMD2 = "mv /opt/massive_correlation/code/stanford-corenlp-full-2018-10-05/$file.txt.json /opt/massive_correlation/data/EnglishDBpediaAbstracts/parsed/$file.json";
		`$CMD2`;
		unlink "/opt/massive_correlation/code/stanford-corenlp-full-2018-10-05/$file.txt";
		print "(MV) > $file.txt.json /opt/massive_correlation/data/EnglishDBpediaAbstracts/parsed/$file.json\n";
	}
}

sub shorten {
	my $string = shift;

	if($string =~ m/\A<http:\/\/dbpedia.org\/resource\/(.*)>/){
		return $1;
	}
	return $string;
}


