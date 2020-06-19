#!/usr/bin/perl -w
use strict;
use YAML qw(LoadFile Dump DumpFile);
use URL::Encode qw(url_decode_utf8);
use Storable;

my $D = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/qald5-1.dat");

my $CFG = {
	M1 => {
		#CC 	=> { 5 => 1 }, 	# Coordinating conjunction
		#CD 	=> { 5 => 1 }, 	# Cardinal number
		#DT 	=> { 5 => 1 }, 	# Determiner
		#EX 	=> { 5 => 1 }, 	# Existential there
		#FW 	=> { 5 => 1 }, 	# Foreign word
		#IN 	=> { 5 => 1 }, 	# Preposition or subordinating conjunction
		JJ 	=> { 0 => 1 }, 	# Adjective
		JJR 	=> { 0 => 1 }, 	# Adjective, comparative
		JJS 	=> { 0 => 1 }, 	# Adjective, superlative
		#LS 	=> { 5 => 1 }, 	# List item marker
		#MD 	=> { 5 => 1 }, 	# Modal
		NN 	=> { 0 => 1 }, 	# Noun, singular or mass
		NNS 	=> { 0 => 1 },	# Noun, plural
		NNP 	=> { 0 => 1 }, 	# Proper noun, singular
		NNPS 	=> { 0 => 1 }, 	# Proper noun, plural
		#PDT 	=> { 5 => 1 }, 	# Predeterminer
		#POS 	=> { 5 => 1 }, 	# Possessive ending
		#PRP 	=> { 5 => 1 }, 	# Personal pronoun
		#"PRP$" => { 5 => 1 }, 	# Possessive pronoun
		RB 	=> { 0 => 1 }, 	# Adverb
		RBR 	=> { 0 => 1 }, 	# Adverb, comparative
		RBS 	=> { 0 => 1 }, 	# Adverb, superlative
		#RP 	=> { 5 => 1 }, 	# Particle
		#SYM 	=> { 5 => 1 }, 	# Symbol
		#TO 	=> { 5 => 1 }, 	# to
		#UH 	=> { 5 => 1 }, 	# Interjection
		VB 	=> { 0 => 1 }, 	# Verb, base form
		VBD 	=> { 0 => 1 }, 	# Verb, past tense
		VBG 	=> { 0 => 1 }, 	# Verb, gerund or present participle
		VBN 	=> { 0 => 1 }, 	# Verb, past participle
		VBP 	=> { 0 => 1 }, 	# Verb, non-3rd person singular present
		VBZ 	=> { 0 => 1 }, 	# Verb, 3rd person singular present
		#WDT 	=> { 5 => 1 }, 	# Wh-determiner
		#WP 	=> { 5 => 1 }, 	# Wh-pronoun
		#"WP$" 	=> { 5 => 1 }, 	# Possessive wh-pronoun
		#WRB 	=> { 5 => 1 }, 	# Wh-adverb 
	},

	M2 => {
		ADJP 	=> { 0 => 1 },
                ADVP 	=> { 0 => 1 },
                NP 	=> { 0 => 1 },
                VP 	=> { 0 => 1 },
		PP 	=> { 0 => 1 },
		#QP maybe later
		#RRC   	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1 },
		UCP   	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1 },

	},

	M2_minimum_string_length =>  5,
	M2_maximum_string_length => 50,
};

my $M = {};

my $cnt = 0;
foreach my $sentence_ID (sort keys %{$D}){

	my $main_entity;
	my $string = url_decode_utf8($sentence_ID);
	$string =~ s/(.*)\/abstract#.*/$1/;
	my $mainentity = "http://dbpedia.org/resource/$string";

	#last if ++$cnt > 1000;

	# convert annotation array into hash
	my $annotations = {};
	foreach my $annotation (@{$D->{$sentence_ID}->{annotations}}){
		for(my $i=$annotation->[0]; $i<=$annotation->[1]; $i++){
			$annotations->{$i} = $annotation; # TODO: can't there be multiple annotations for a token?
		}
	}

	if(exists $CFG->{M1} and scalar keys %{$CFG->{M1}}){
		for(my $token_ID=0; $token_ID < scalar @{$D->{$sentence_ID}->{tokens}}; $token_ID++){
			my $postag = $D->{$sentence_ID}->{postags}->[$token_ID];
			if(exists $CFG->{M1}->{$postag}){
				$M
                                	->{M1}
                                	->{$postag}
					->{-1}
                                	->{$D->{$sentence_ID}->{tokens}->[$token_ID]->[0]} = undef;
			}
		}
		#print Dump { M => $M }; <STDIN>;
	} # M1

        if(exists $CFG->{M2} and scalar keys %{$CFG->{M2}}){
		my $string = $D->{$sentence_ID}->{parse};
		my @parts = split("\n", $string);
		foreach my $part (@parts){
        		$part =~ s/\A\s*//;
		}
		$string = join(' ', @parts);

		# add token numbers and token separators ("after") to parse string
		my $tokenID = 0;
		foreach my $tokenplus (@{$D->{$sentence_ID}->{tokens}}){
        		my $token = $tokenplus->[2];
       		 	my $after = $tokenplus->[1];
        		#print "[$tokenID] $token\n";
        		my $token_qm = quotemeta $token;
        		if($string =~ m/\s$token_qm/){
                		#print "found\n";
                		$string =~ s/\s$token_qm/ <<<$token-$tokenID $after>>>/;
                		#print "$string\n"; <STDIN>;
        		} else {
                                # TODO: proper error handling. continue elsewhere.
                		print "not found!\n";
                		print Dump $tokenplus;
                		print "$string\n"; <STDIN>;
        		}
       	 		$tokenID++;
		}

		my @matches = $string =~ /\((?:\(.*?\)|[^\(])*?\)|\w+/sg;

		foreach my $match (@matches){
			FET: foreach my $tag (keys %{$CFG->{M2}}){
				if($match =~ m/\A\($tag/){
					#print " ::::::::: $tag | $match\n";

					my @tokens = $match =~ /<<<(.+?)>>>/g;
					if(not scalar @tokens){
						#print Dump {
						#	tag => $tag,
						#	string => $string,
						#}; <STDIN>;
						# unclear why this case ever occurs. however, it seems to occur rarely. just ignore it.
						next FET;
					}
					$tokens[0] =~ m/-(\d+)\D+$/;
					my $first_token_position = $1;
					$tokens[-1] =~ m/-(\d+)\D+$/;
					my $last_token_position = $1;
					my @parts = ();

					for(my $i=0; $i<scalar @tokens; $i++){
						if($tokens[$i] =~ m/(.*)-\d+\s(\D*)$/){
							if($i < $#tokens){
								push(@parts, "$1$2");
							} else {
								push(@parts, $1);
							}
						} else {
							# TODO: proper error handling. continue elsewhere.
							print "unexpected token format: <" . $tokens[$i] . ">\n"; <STDIN>;
						}
					}

					my $string = join("", @parts);

					if(
						length($string) < $CFG->{M2_minimum_string_length} or
                                                length($string) > $CFG->{M2_maximum_string_length} or
						$string =~ m/\A\d+\Z/
					){
						# ignore match
					} else {

						$M
                                        		->{M2}
                                        		->{$tag}
							->{-1}
                                        		->{$string} = undef;
					}
				}
			}
		}

		#print Dump { M => $M }; <STDIN>;
	} # M2

	# TODO M3
	
	# TODO M4
}

print "\n";

DumpFile("/opt/massive_correlation/data/EnglishDBpediaAbstracts/qald5-2.yml", $M);
store $M, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/qald5-2.dat";

#print Dump $M;
