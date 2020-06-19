#!/usr/bin/perl -w
use strict;
#use YAML qw(LoadFile Dump DumpFile);
use Storable;

my $CFG = {
	M1 => {
                #CC 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Coordinating conjunction
                #CD 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Cardinal number
                #DT 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Determiner
                #EX 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Existential there
                #FW 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Foreign word
                #IN 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Preposition or subordinating conjunction
		JJ 	=> { 
			5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,
			#-1 => 1
		},  # Adjective
		JJR 	=> {
			5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,
			#-1 => 1
		},  # Adjective, comparative
		JJS 	=> { 
			5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, 
			#-1 => 1
		},  # Adjective, superlative
                #LS 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # List item marker
                #MD 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Modal
		NN 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Noun, singular or mass
		NNS 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Noun, plural
		NNP 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Proper noun, singular
		NNPS 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Proper noun, plural
		#PDT 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Predeterminer
                #POS 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Possessive ending
                #PRP 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Personal pronoun
                #"PRP$" => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Possessive pronoun
		RB 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Adverb
		RBR 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Adverb, comparative
		RBS 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Adverb, superlative
                #RP 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Particle
                #SYM 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Symbol
                #TO 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # to
                #UH 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Interjection
		VB 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, },#-1 => 1 },  # Verb, base form
		VBD 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, },#-1 => 1 },  # Verb, past tense
		VBG 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, },#-1 => 1 },  # Verb, gerund or present participle
		VBN 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, },#-1 => 1 },  # Verb, past participle
		VBP 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, },#-1 => 1 },  # Verb, non-3rd person singular present
		VBZ 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, },#-1 => 1 },  # Verb, 3rd person singular present
                #WDT 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Wh-determiner
		#WP 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Wh-pronoun
                #"WP$" 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Possessive wh-pronoun
                #WRB 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },  # Wh-adverb 
	},

	M2 => {
		ADJP    => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Adjective Phrase
		ADVP    => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Adverb Phrase
		NP      => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Noun Phrase
		VP      => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },  # Verb Phrase
		PP      => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },
                #QP maybe later
		#RRC     => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1 },
		UCP     => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1,},# -1 => 1 },
	}
};

my $pattern_to_setID = {};
my $setID_to_patterns = {};
my $setID_to_entities = {};
#my $entity_to_setIDs = {};

my $entities_to_setID = {}; # part of the bad hack

my $last_setID = -1;

#print "warning, reduced to M1 and only a few postags, x-files.\n"; <STDIN>;


# M1
foreach my $alpha (keys %{$CFG->{M1}}){ #next if $alpha ne "JJ";
	
	next if not -e "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en-M1-$alpha.dat";
	print "with alpha=$alpha\n";
	
	my $M = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en-M1-$alpha.dat");

        foreach my $beta (keys %{$CFG->{M1}->{$alpha}}){ #print "  with beta=$beta\n";
        	if(exists $M->{M1}->{$alpha}->{$beta}){
                	foreach my $token (keys %{$M->{M1}->{$alpha}->{$beta}}){ #print "    with token=$token\n";
				my $entities = $M->{M1}->{$alpha}->{$beta}->{$token}->{entities};

				# check whether this set of entities has been seen before
				# TODO this is a very bad hack
				my $string_of_entities = join("==", sort keys %{$entities});
				my $setID;
				if(exists $entities_to_setID->{$string_of_entities}){
					$setID = $entities_to_setID->{$string_of_entities};
				} else {
					$last_setID++;
					$setID = "M1-$alpha-$beta-" . $last_setID;
					$entities_to_setID->{$string_of_entities} = $setID;
				}

				$pattern_to_setID->{M1}->{$alpha}->{$beta}->{$token} = $setID;
				$setID_to_patterns->{$setID}->{M1}->{$alpha}->{$beta}->{$token} = 1;
				$setID_to_entities->{$setID}->{$_} = 1 foreach (keys %{$entities});
				#foreach my $entity (keys %{$entities}){
				#	$entity_to_setIDs->{$entity}->{$setID} = 1;
				#}
			}
		}
		store $pattern_to_setID->{M1}->{$alpha}->{$beta}, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/pattern_to_setIDs_en-M1-$alpha-$beta.dat";
		#DumpFile("/opt/massive_correlation/data/EnglishDBpediaAbstracts/pattern_to_setIDs_en-M1-$alpha-$beta.yml", $pattern_to_setID->{M1}->{$alpha}->{$beta});
	}
}

$last_setID = -1;

# M2
foreach my $alpha (keys %{$CFG->{M2}}){ #next if $alpha ne "JJ";

        next if not -e "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en-M2-$alpha.dat";
        print "with alpha=$alpha\n";

        my $M = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en-M2-$alpha.dat");

        foreach my $beta (keys %{$CFG->{M2}->{$alpha}}){ #print "  with beta=$beta\n";
                if(exists $M->{M2}->{$alpha}->{$beta}){
                        foreach my $token (keys %{$M->{M2}->{$alpha}->{$beta}}){ #print "    with token=$token\n";
                                my $entities = $M->{M2}->{$alpha}->{$beta}->{$token}->{entities};

                                # check whether this set of entities has been seen before
                                # TODO this is a very bad hack
                                my $string_of_entities = join("==", sort keys %{$entities});
                                my $setID;
                                if(exists $entities_to_setID->{$string_of_entities}){
                                        $setID = $entities_to_setID->{$string_of_entities};
                                } else {
                                        $last_setID++;
                                        $setID = "M2-$alpha-$beta-" . $last_setID;
                                        $entities_to_setID->{$string_of_entities} = $setID;
                                }

                                $pattern_to_setID->{M2}->{$alpha}->{$beta}->{$token} = $setID;
                                $setID_to_patterns->{$setID}->{M2}->{$alpha}->{$beta}->{$token} = 1;
                                $setID_to_entities->{$setID}->{$_} = 1 foreach (keys %{$entities});
                                #foreach my $entity (keys %{$entities}){
                                #       $entity_to_setIDs->{$entity}->{$setID} = 1;
                                #}
                        }
                }
		store $pattern_to_setID->{M2}->{$alpha}->{$beta}, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/pattern_to_setIDs_en-M2-$alpha-$beta.dat";
		#DumpFile("/opt/massive_correlation/data/EnglishDBpediaAbstracts/pattern_to_setIDs_en-M2-$alpha-$beta.yml", $pattern_to_setID->{M2}->{$alpha}->{$beta});

        }
}

store $setID_to_entities, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/setID_to_entities_en.dat";
store $setID_to_patterns, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/setID_to_patterns_en.dat";
