#!/usr/bin/perl -w
use strict;
use YAML qw(LoadFile Dump DumpFile);
use Storable;

my $CFG = {
	M1 => {
		#CC 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Coordinating conjunction
		#CD 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Cardinal number
		#DT 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Determiner
		#EX 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Existential there
		#FW 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Foreign word
		#IN 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Preposition or subordinating conjunction
		# done JJ 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Adjective
		# done JJR 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Adjective, comparative
		# doneJJS 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Adjective, superlative
		#LS 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # List item marker
		#MD 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Modal
		# done NN 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Noun, singular or mass
		# done NNS 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Noun, plural
		# done NNP 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Proper noun, singular
		# done NNPS 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Proper noun, plural
		#PDT 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Predeterminer
		#POS 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Possessive ending
		#PRP 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Personal pronoun
		#"PRP$" 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Possessive pronoun
		# done RB 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Adverb
		# done RBR 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Adverb, comparative
		# done RBS 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Adverb, superlative
		#RP 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Particle
		#SYM 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Symbol
		#TO 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # to
		#UH 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Interjection
		# done VB 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Verb, base form
		# done VBD 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Verb, past tense
		# done VBG 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Verb, gerund or present participle
		# done VBN 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Verb, past participle
		# done VBP 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Verb, non-3rd person singular present
		# done VBZ 	=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Verb, 3rd person singular present
		#WDT 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Wh-determiner
		#WP 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Wh-pronoun
		#"WP$" 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Possessive wh-pronoun
		#WRB 		=> { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },   # Wh-adverb 
	},
	M1_min_number_of_entities => 10, # or, do I need a minimum number for each postag?

	M2 => { 
		# done ADJP    => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 }, # Adjective Phrase
		# done ADVP    => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 }, # Adverb Phrase
		# done NP      => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 }, # Noun Phrase
		# done VP      => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 }, # Verb Phrase
		# done PP      => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },
		#QP maybe later
		# done RRC    => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },
		# done UCP     => { 5 => 1, 4 => 1, 3 => 1, 2 => 1, 1 => 1, 0 => 1, -1 => 1 },
	},
	M2_min_number_of_entities => 10, # or, do I need a minimum number for each phrase tag?


};

#print "warning, reduced to subcorpus (i.e., 0.109) and M1 and few postags\n"; <STDIN>;

# M1
foreach my $alpha (keys %{$CFG->{M1}}){ #next if $alpha ne "JJ";
	print "(M1) with alpha=$alpha\n";

	my $M = {};

	for(my $i=0; $i<=109; $i++){ # 0..109
		next if not -e "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en$i-2.dat";

		print "(M1) <LOAD abstracts_en$i-2.dat\n";
		my $D = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en$i-2.dat");
		print "(M1) LOAD>\n";

		if(exists $D->{M1} and exists $D->{M1}->{$alpha}){
			foreach my $beta (keys %{$CFG->{M1}->{$alpha}}){ #print "  with beta=$beta\n";
				if(exists $D->{M1}->{$alpha}->{$beta}){
					foreach my $token (keys %{$D->{M1}->{$alpha}->{$beta}}){ #print "    with token=$token\n";
						
						# ignore all tokens that contain numbers (e.g., 11th, 17th-century, 10-day.
						# TODO: no good reason why to ignore these. maybe, allow these tokens in the future

						next if $token =~ m/\d/;

						next if $token eq "=";

						next if length($token) <= 3;

						foreach my $entity (keys %{$D->{M1}->{$alpha}->{$beta}->{$token}->{entities}}){
							next if $entity !~ m/\Ahttp:\/\/dbpedia\.org\//; # why? because DBpedia does not contain information about external entities
							#print "      with entity=$entity\n";
							foreach my $sentence (keys %{$D->{M1}->{$alpha}->{$beta}->{$token}->{entities}->{$entity}}){
								$M->{M1}->{$alpha}->{$beta}->{$token}->{entities}->{$entity}->{$sentence} +=
									$D->{M1}->{$alpha}->{$beta}->{$token}->{entities}->{$entity}->{$sentence};
							}
						}

						if($beta != -1){
							foreach my $entity (keys %{$D->{M1}->{$alpha}->{-1}->{$token}->{entities}}){
								next if $entity !~ m/\Ahttp:\/\/dbpedia\.org\//; # why? because DBpedia does not contain information about external entities
								#print "      with entity=$entity\n";
								foreach my $sentence (keys %{$D->{M1}->{$alpha}->{-1}->{$token}->{entities}->{$entity}}){
									$M->{M1}->{$alpha}->{$beta}->{$token}->{entities}->{$entity}->{$sentence} +=
									$D->{M1}->{$alpha}->{-1}->{$token}->{entities}->{$entity}->{$sentence};
								}
							}
						}
					}
				}
			}
		}
	}

	# pruning
	print "(M1) pruning ...\n";
	foreach my $beta (keys %{$CFG->{M1}->{$alpha}}){
		if(exists $M->{M1}->{$alpha}->{$beta}){
                	foreach my $token (keys %{$M->{M1}->{$alpha}->{$beta}}){ #print "    with token=$token\n";
				if(scalar keys %{$M->{M1}->{$alpha}->{$beta}->{$token}->{entities}} < $CFG->{M1_min_number_of_entities}){
					delete $M->{M1}->{$alpha}->{$beta}->{$token};
				}
			}
		}
	}
	print "(M1) ... done\n";
	print "(M1) storing ...\n";
	store $M, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en-M1-$alpha.dat";
	#DumpFile("/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en-M1-$alpha.yml", $M);
	print "(M1) ... done\n";
}

#M2
foreach my $alpha (keys %{$CFG->{M2}}){
	print "(M2) with alpha=$alpha\n";

	my $M = {};

	for(my $i=0; $i<=109; $i++){ # 0..109
		next if not -e "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en$i-2.dat";

		print "(M2) <LOAD abstracts_en$i-2.dat\n";
		my $D = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en$i-2.dat");
		print "(M2)  LOAD>\n";

		if(exists $D->{M2} and exists $D->{M2}->{$alpha}){
			foreach my $beta (keys %{$CFG->{M2}->{$alpha}}){ #print "  with beta=$beta\n";
				if(exists $D->{M2}->{$alpha}->{$beta}){
					foreach my $token (keys %{$D->{M2}->{$alpha}->{$beta}}){ #print "    with token=$token\n";

						# ignore all tokens that contain numbers (e.g., 11th, 17th-century, 10-day.
						# TODO: no good reason why to ignore these. maybe, allow these tokens in the future

						next if $token =~ m/\d/;

						foreach my $entity (keys %{$D->{M2}->{$alpha}->{$beta}->{$token}->{entities}}){
							next if $entity !~ m/\Ahttp:\/\/dbpedia\.org\//;
							#print "      with entity=$entity\n";
							foreach my $sentence (keys %{$D->{M2}->{$alpha}->{$beta}->{$token}->{entities}->{$entity}}){
								$M->{M2}->{$alpha}->{$beta}->{$token}->{entities}->{$entity}->{$sentence} +=
								$D->{M2}->{$alpha}->{$beta}->{$token}->{entities}->{$entity}->{$sentence};
							}
						}

						if($beta != -1){
							foreach my $entity (keys %{$D->{M2}->{$alpha}->{-1}->{$token}->{entities}}){
								next if $entity !~ m/\Ahttp:\/\/dbpedia\.org\//; # why? because DBpedia does not contain information about external entities
								#print "      with entity=$entity\n";
								foreach my $sentence (keys %{$D->{M2}->{$alpha}->{-1}->{$token}->{entities}->{$entity}}){
									$M->{M2}->{$alpha}->{$beta}->{$token}->{entities}->{$entity}->{$sentence} +=
									$D->{M2}->{$alpha}->{-1}->{$token}->{entities}->{$entity}->{$sentence};
								}
							}
						}
					}
				}
			}
		}
	}

	# pruning
	print "(M2) pruning ...\n";
	foreach my $beta (keys %{$CFG->{M2}->{$alpha}}){
		if(exists $M->{M2}->{$alpha}->{$beta}){
			foreach my $token (keys %{$M->{M2}->{$alpha}->{$beta}}){ #print "    with token=$token\n";
				if(scalar keys %{$M->{M2}->{$alpha}->{$beta}->{$token}->{entities}} < $CFG->{M2_min_number_of_entities}){
					delete $M->{M2}->{$alpha}->{$beta}->{$token};
				}
			}
		}
	}
	print "(M2) ... done\n";
	print "(M2) storing ...\n";
	store $M, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en-M2-$alpha.dat";
	#DumpFile("/opt/massive_correlation/data/EnglishDBpediaAbstracts/abstracts_en-M2-$alpha.yml", $M);
	print "(M2) ... done\n";
}
																																					#

