#!/usr/bin/perl -w
use strict;
use YAML qw(Dump DumpFile);
use Clone qw(clone);
use Storable;

# create configuration file for mc-lamgrapami

my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime();
my $timestring = sprintf("%04d-%02d-%02d_", 1900+$year, $mon, $mday) . sprintf("%02d-%02d-%02d", $hour, $min, $sec);

print "load patterns ...\n";
my $patterns = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/qald5-2.dat");
print " ...done\n";

my $sampleToN = 1000;


my $files = {
	#"article-categories_en.nt"                     => "A",
	#"category-labels_en.nt"                        => "C",
	"dbpedia_2015-04.nt"                            => "D",
	"infobox-properties_en.nt"                      => "I1",
	#"infobox-property-definitions_en.nt"           => "I2",
	#"instance-types-transitive_en.nt"              => "I3",
	"instance-types_en.nt"                          => "I4",
	#"instance_types_sdtyped-dbo_en.nt"             => "I5",
	#"labels_en.nt"                                 => "L",
	"mappingbased-properties_en.nt"                 => "M",
	#"skos-categories_en.nt"                        => "S1",
	#"specific-mappingbased-properties_en.nt"       => "S2",
};


my $CFG = {

	support_measure					=> "MIS_e_emb",
	tau						=> 10,
	minimum_pattern_size				=> 2,
	maximum_pattern_size				=> 5,
	maximum_number_of_input_lines			=> 500000,

	evaluation_strategy				=> "strategy-5bf",
	maximum_number_of_independent_sets		=> 1000000,
	
	print_frequent_triple_patterns			=> 0, 
	print_frequent_triple_patterns_with_mappings 	=> 0,
	print_frequent_patterns				=> 1,
	print_frequent_patterns_with_mappings		=> 0,
	print_total_number_of_mappings			=> 0,
	
	store_frequent_triple_patterns			=> 0,
	store_frequent_triple_patterns_with_mappings 	=> 0,
	store_frequent_patterns				=> 1,
	store_frequent_patterns_with_mappings 		=> 0,

	shorten_terms 					=> 1, 

	input_file					=> undef, # will be set later
	output_file					=> undef, # will be set later
	incremental_output_file				=> undef, # will be set later

	allowed_abstractions => {
		s => 1,
		p => 1,
		o => 1,
		sp => 1,
		so => 1,
		po => 1,
		spo => 0,
		xxx => 0,
		xxy => 0,
		xyx => 0,
		xyy => 0,
		xyz => 0,
	},

	allowed_join_types => {
		"s-s" => 1,
		"s-p" => 1,
		"s-o" => 1,
		"p-s" => 1,
		"p-p" => 1,
		"p-o" => 1,
		"o-s" => 1,
		"o-p" => 1,
		"o-o" => 1,	
	},

	ignore_patterns => {
		"label" 			=> 1, 
		"equivalentClass" 		=> 1, 
		"wasDerivedFrom" 		=> 1, 
		"comment" 			=> 1, 
		"wikidata" 			=> 1, 
		"Thing" 			=> 1, 
		"ontologydesignpatterns" 	=> 1,
	},

	verbose 				=> 0,

	measurements => {
		time 				=> 1,
		growth 				=> 1,
		total_number_of_mappings 	=> 1,
		incompatible_mappings 		=> 1,
		compatible_mappings 		=> 1,
	},


	prefix_definitions => {
		"http://www.w3.org/1999/02/22-rdf-syntax-ns#" 			=> "rdf",
		"http://www.w3.org/2000/01/rdf-schema#" 			=> "rdfs",
		"http://dbpedia.org/resource/" 					=> "dbr",
		"http://dbpedia.org/ontology/" 					=> "dbo",
		"http://dbpedia.org/property/" 					=> "dbp",
		"http://purl.org/dc/elements/1.1/" 				=> "dc",
		"http://xmlns.com/foaf/0.1/" 					=> "foaf",
		"http://yago-knowledge.org/resource/" 				=> "yago",
		"http://www.w3.org/2004/02/skos/core#" 				=> "skos",
		"http://schema.org/" 						=> "schema",
		"http://www.wikidata.org/prop/direct-normalized/" 		=> "wdtn",
		"http://www.wikidata.org/entity/" 				=> "wd",
		"http://www.wikidata.org/entity/statement/" 			=> "wds",
		"http://www.wikidata.org/value/" 				=> "wdv",
		"http://www.wikidata.org/prop/direct/" 				=> "wdt",
		"http://wikiba.se/ontology#" 					=> "wikibase",
		"http://www.wikidata.org/prop/" 				=> "wp",
		"http://www.wikidata.org/prop/statement/" 			=> "wps",
		"http://www.wikidata.org/prop/qualifier/" 			=> "wpq",
		"http://www.w3.org/2002/07/owl#"				=> "owl",
		"http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#" 	=> "odp",
		"http://www.w3.org/2001/XMLSchema#"				=> "xsd",
		"http://www.w3.org/ns/prov#"					=> "prov", 
		"http://psink.de/scio/"						=> "scio",
		"http://scio/data/"						=> "sciodata",
		"http://www.example.org/"					=> "ex",
	},
	worker_count => 10,
	batch_size => 1,
	measure_pattern_has_been_seen_before => 1,
	measure_growth => 1,
	measure_time_in_evaluation => 1,
	runtime_output => "../results/runtime-$timestring.json",

	context => "massivecorrelation",
	linguistic_pattern			=> undef, # will be set later
	set_of_entities 			=> [], # will be populated later
};

my $string = join(q{}, sort values %{$files});

#my $new_linguistic_patterns = {}; # abstracts from concrete methods

print "load setID_to_entities_en.dat ...\n";
my $S = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/setID_to_entities_en.dat");
print " ...done\n";

mkdir "/opt/massive_correlation/data/dbpedia-2015-04/nhops"
        if not -d "/opt/massive_correlation/data/dbpedia-2015-04/nhops";
mkdir "/opt/massive_correlation/data/lamgrapami-cfg"
        if not -d "/opt/massive_correlation/data/lamgrapami-cfg";



my $use_tokens = {};

# proper adjetives
foreach (qw(
	American
	Canadian
	Dutch
	German
	Greek
	Methodist
	Polish
	communist
	nobel
	olympic
)){
	$use_tokens->{$_} = 1;
}

# demonstrative adjectives
foreach (qw(
	last
	worst
	highest
	largest
	latest
	longest
	past
	prime
	youngest
)){
	$use_tokens->{$_} = 1;
}

# descriptive adjectives
foreach (qw(
	average
	beautiful
	big
	comic
	earlier
	federal
	female
	former
	given
	high
	higher
	military
	official
	professional
	tall
	total
)){
	$use_tokens->{$_} = 1;
}

# M1
if(exists $patterns->{M1}){
	foreach my $alpha (keys %{$patterns->{M1}}){
		#next if $alpha !~ m/\AJJ|JJS|JJR\Z/; # TODO remove
		print "with alpha <$alpha>\n";
		foreach my $beta (keys %{$patterns->{M1}->{$alpha}}){
			print " with beta <$beta>\n";
			foreach my $token (sort keys %{$patterns->{M1}->{$alpha}->{$beta}}){

				next if not exists $use_tokens->{$token};

                                print "  with token <$token>\n";

                        	my $M = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/pattern_to_setIDs_en-M1-$alpha-$beta.dat");

				if(exists $M->{$token}){
					my $setID = $M->{$token};
					my $entities = &select_entities($S->{$setID});
				
					if(not -e "/opt/massive_correlation/data/dbpedia-2015-04/nhops/M1-$alpha-$beta-$token-$sampleToN.txt"){
						open(OUT,">/opt/massive_correlation/data/dbpedia-2015-04/nhops/M1-$alpha-$beta-$token-$sampleToN.txt");
						print OUT "<$_>\n" foreach @{$entities};
						close OUT;
						print " > /opt/massive_correlation/data/dbpedia-2015-04/nhops/M1-$alpha-$beta-$token-$sampleToN.txt\n";
					}

					#if(not -e "/opt/massive_correlation/data/dbpedia-2015-04/nhops/2hops_M1-$alpha-$beta-$token-$sampleToN-$string.nt"){
					#	print "2hops.sh M1-$alpha-$beta-$token-$sampleToN ...\n";
					#	my $CMD = "bash /opt/massive_correlation/data/dbpedia-2015-04/nhops/2hops.sh /opt/massive_correlation/data/dbpedia-2015-04/${string}_sorted_unique.nt M1-$alpha-$beta-$token-$sampleToN.txt M1-$alpha-$beta-$token-$sampleToN-$string";
					#	print "CMD $CMD\n";
					#	my $res = system($CMD);
					#	print " -> $res\n";
					#}


					if(not -e "/opt/massive_correlation/data/dbpedia-2015-04/nhops/3hops_M1-$alpha-$beta-$token-$sampleToN-$string.nt"){
                                                print "3hopsP.sh M1-$alpha-$beta-$token-$sampleToN ...\n";
                                                my $CMD = "bash /opt/massive_correlation/data/dbpedia-2015-04/nhops/3hopsP.sh /opt/massive_correlation/data/dbpedia-2015-04/${string}_sorted_unique.nt M1-$alpha-$beta-$token-$sampleToN.txt M1-$alpha-$beta-$token-$sampleToN-$string";
                                                print "CMD $CMD\n";
                                                my $res = system($CMD);
                                                print " -> $res\n";
                                        }

					my $a = $CFG->{tau};
					my $b = $CFG->{maximum_number_of_input_lines};
					my $c = $CFG->{maximum_number_of_independent_sets};

					my $CFG_filename = "/opt/massive_correlation/data/lamgrapami-cfg/lamgrapami-p-filtered-M1-$alpha-$beta-$token-$sampleToN-$string-$a-$b-$c";
					if(not -e "$CFG_filename.yml"){
						my $this_CFG = clone($CFG);
						$this_CFG->{linguistic_pattern} = "M1-$alpha-$beta-$token";
						$this_CFG->{set_of_entities} = $entities;
						$this_CFG->{input_file} = "/opt/massive_correlation/data/dbpedia-2015-04/nhops/3hops_M1-$alpha-$beta-$token-$sampleToN-$string.nt";
						$this_CFG->{output_file} = "/opt/massive_correlation/results/results-M1-$alpha-$beta-$token-$sampleToN-$string-$a-$b-$c.txt",
						$this_CFG->{incremental_output_file} ="/opt/massive_correlation/results/incremental-results-M1-$alpha-$beta-$token-$sampleToN-$string-$a-$b-$c.txt",
					
						DumpFile("$CFG_filename.yml", $this_CFG); #print "wait"; <STDIN>;
						#store $this_CFG, "$CFG_filename.dat";
						print " > $CFG_filename\n";
					}

					#foreach my $entity (keys %{$S->{$setID}}){ #print "($setID $alpha $beta $token) push $entity\n";
					#	push(@{$CFG->{sets_of_entities}->{$setID}}, $entity);
					#}
				} else {
					print "No setID known for <M1> <$alpha> <$beta> <$token>\n";
				}
			}
		}
	}
}


# M2
if(0 and exists $CFG->{linguistic_patterns}->{M2}){ # TODO remove 0 and
	if(1
	#	not $name eq "adjonly" and
	#	not $name eq "canadianonly" and 
	#	not $name eq "americanonly" and 
	#	not $name eq "professionalonly" and
	#	not $name eq "greekonly" and
	#	not $name eq "highestonly"
	#	not $name eq "dutchonly" and
	#	not $name eq "germanonly"
	){
		foreach my $alpha (keys %{$patterns->{M2}}){ print "with alpha <$alpha>\n";
			foreach my $beta (keys %{$patterns->{M2}->{$alpha}}){ print " with beta <$beta>\n";
				foreach my $token (keys %{$patterns->{M2}->{$alpha}->{$beta}}){
					my $M = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/pattern_to_setIDs_en-M2-$alpha-$beta.dat");

					# find and add setID to CFG
					if(exists $M->{$token}){

						# TODO> create n-hop-cover, create CFG file, ...

						my $setID = $M->{$token};
						$CFG->{linguistic_patterns}->{M2}->{$alpha}->{$beta}->{$token} = $setID;
						####$new_linguistic_patterns->{"$token (M2 $alpha $beta)"} = $setID;
						# collect necessary sets of entities
						foreach my $entity (keys %{$S->{$setID}}){
							push(@{$CFG->{sets_of_entities}->{$setID}}, $entity);
						}
					} else {
						print "No setID known for <M2> <$alpha> <$beta> <$token>\n";
					}
				}
			}
		}
	}
}


#$CFG->{linguistic_patterns} = $new_linguistic_patterns;
#
#print Dump $CFG; <STDIN>;
#DumpFile("/opt/massive_correlation/data/EnglishDBpediaAbstracts/lamgrapami-$name-$sampleToN-$timestring.yml", $CFG);
#store $CFG, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/lamgrapami-$name-$sampleToN-$timestring.dat";
#print " > /opt/massive_correlation/data/EnglishDBpediaAbstracts/lamgrapami-$name-$sampleToN-$timestring.dat\n";

sub select_entities {
	my $hash = shift;
	my @array = keys %{$hash};
	if(not defined $sampleToN or scalar keys %{$hash} < $sampleToN){
		return [keys %{$hash}]
	} else {
		my $set = [];
		my $selected_h = {};
		while(scalar @{$set} < $sampleToN){
			my $entity = $array[rand scalar @array];
			if(not exists $selected_h->{$entity}){
				push (@{$set}, $entity);
			       	$selected_h->{$entity} = 1;
			}
		}
		return $set;
	}
}
