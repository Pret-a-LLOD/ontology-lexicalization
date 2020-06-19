#!/usr/bin/perl -w
use strict;
use YAML qw(Dump DumpFile);
use Storable;

# create configuration file for mc-lamgrapami

my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime();
my $timestring = sprintf("%04d-%02d-%02d_", 1900+$year, $mon, $mday) . sprintf("%02d-%02d-%02d", $hour, $min, $sec);

# TODO: distinguish between own configuration and lamgrapami configuration

my $patterns = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/qald5-2.dat");
#my $name = "adjonly";
my $name = "canadianonly";
#my $name = "americanonly";
#my $name = "professionalonly";
#my $name = "greekonly";
#my $name = "highestonly";
#my $name = "germanonly";
#my $name = "dutchonly";
#nationalonly JJ
#youngestonly JJS
#latestonly JJS
#militaryonly JJ

print " <<< $name >>>\n";

my $sampleToN = 1000;

my $CFG = {

	context => "massivecorrelation",

	files => {
		#"article-categories_en.nt" 			=> "A",
		#"category-labels_en.nt" 			=> "C",
		"dbpedia_2015-04.nt" 				=> "D",
		"infobox-properties_en.nt" 			=> "I1", 
		#"infobox-property-definitions_en.nt" 		=> "I2",
		#"instance-types-transitive_en.nt" 		=> "I3",
		"instance-types_en.nt" 				=> "I4",
		#"instance_types_sdtyped-dbo_en.nt" 		=> "I5",
		#"labels_en.nt" 				=> "L",
		"mappingbased-properties_en.nt" 		=> "M",
		#"skos-categories_en.nt" 			=> "S1",
		#"specific-mappingbased-properties_en.nt" 	=> "S2",
	},
	linguistic_patterns => #{ # TODO: load from files (not necessarily sets_of_entities_en-M1-JJ-5.yml, could be a subset according to QALD)
		$patterns,
		#M1 => {
		#	"JJR" => {
		#		-1 => {
		#			"earlier" 	=> undef,
		#			"more" 		=> undef,
		#		},
		#	},
		#	"JJS" => {
		#		-1 => {
		#			"highest" 	=> undef, ##
		#			"largest" 	=> undef,
		#			"latest" 	=> undef,
		#			"longest" 	=> undef,
		#			"youngest" 	=> undef, ##
		#			"Worst" 	=> undef,
		#			"worst" 	=> undef,
		#			"most" 		=> undef,
		#		},
		#	},
		#	"JJ" => {
		#		-1 => {
		#			# QALD-5 adjectives
		#			#"16th"		=> undef,
		#			"American"	=> undef, ##
		#			"average"	=> undef,
		#			"beautiful"	=> undef,
		#			#"big"		=> undef,
		#			"Canadian"	=> undef, ##
		#			"communist"	=> undef,
		#			"Dutch"		=> undef, ##
		#			"federal"	=> undef,
		#			"female"	=> undef,
		#			"first"		=> undef,
		#			"former"	=> undef,
		#			"German"	=> undef, ##
		#			"given"		=> undef,
		#			"Greek"		=> undef, ##
		#			"Last"		=> undef,
		#			"main"		=> undef,
		#			"many"		=> undef,
		#			"Methodist"	=> undef,
		#			"military"	=> undef,
		#			"national"	=> undef, ##
		#			"official"	=> undef,
		#			"other"		=> undef,
		#			"past"		=> undef,
		#			"Polish"	=> undef,
		#			"prime"		=> undef,
		#			"professional"	=> undef, ##
		#			"same"		=> undef,
		#			"second"	=> undef,
		#			"tall"		=> undef,
		#			"total"		=> undef,
		#		}
		#	}
		#}
	#},
	sets_of_entities => {}, # will be loaded from file
	max_tau => 100,
	rel_tau => 0.3,

	support_measure 				=> "MIS_e_mu",
	minimum_pattern_size 				=> 2,
	maximum_pattern_size 				=> 15,
	maximum_number_of_input_lines 			=> 100000,
	
	evaluation_strategy				=> "strategy-2bf",

	print_frequent_triple_patterns			=> 0, 
	print_frequent_triple_patterns_with_mappings 	=> 0,
	print_frequent_patterns				=> 1,
	print_frequent_patterns_with_mappings		=> 0,
	print_total_number_of_mappings			=> 1,
	
	store_frequent_triple_patterns			=> 0,
	store_frequent_triple_patterns_with_mappings 	=> 0,
	store_frequent_patterns				=> 1,
	store_frequent_patterns_with_mappings 		=> 0,

	shorten_terms 					=> 1, 

	input_file					=> undef,

	output_file					=> "/opt/massive_correlation/results/results-$name-$sampleToN-$timestring.txt",
							#"/xfs01/work/basile/massive_correlation/results/results-$name-$sampleToN-$timestring.txt",
        incremental_output_file                         => "/opt/massive_correlation/results/incremental-results-$name-$sampleToN-$timestring.txt",
							#"/xfs01/work/basile/massive_correlation/results/incremental-results-$name-$sampleToN-$timestring.txt",

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
		"s-p" => 0,
		"s-o" => 1,
		"p-s" => 0,
		"p-p" => 1,
		"p-o" => 0,
		"o-s" => 1,
		"o-p" => 0,
		"o-o" => 1,	
	},

	ignore_patterns => {},

	verbose => 1,

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
		"http://example.org/"						=> "ex",
	},
};

my $string = join(q{}, sort values %{$CFG->{files}});
$CFG->{input_file} = "/opt/massive_correlation/data/dbpedia-2015-04/${string}_sorted_unique.nt";
#die "nt file <" . $CFG->{input_file} . "> does not exist!" if not -e $CFG->{input_file};
#$CFG->{input_file} = "/xfs01/work/basile/massive_correlation/data/dbpedia-2015-04/${string}_sorted_unique.nt";

my $new_linguistic_patterns = {}; # abstracts from concrete methods

my $S = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/setID_to_entities_en.dat");

# M1
if(exists $CFG->{linguistic_patterns}->{M1}){
	foreach my $alpha (keys %{$CFG->{linguistic_patterns}->{M1}}){
		
		if(
			$name eq "adjonly" or
			$name eq "canadianonly" or
			$name eq "americanonly" or
			$name eq "professionalonly" or
			$name eq "greekonly" or
			$name eq "germanonly"or
			$name eq "highestonly" or
			$name eq "dutchonly"
		){
			next if $alpha !~ m/\AJJ|JJS|JJR\Z/; print "with alpha <$alpha>\n";
		}
		
		foreach my $beta (keys %{$CFG->{linguistic_patterns}->{M1}->{$alpha}}){ print " with beta <$beta>\n";
			foreach my $token (keys %{$CFG->{linguistic_patterns}->{M1}->{$alpha}->{$beta}}){
				if($name eq "canadianonly" and $token ne "Canadian"){
					next;
				}
				if($name eq "americanonly" and $token ne "American"){
					next;
				}
				#if($name eq "professionalonly" and $token ne "professional"){
				#	next;
				#}
				if($name eq "greekonly" and $token ne "Greek"){
				        next;
				}
				#if($name eq "highestonly" and $token ne "highest"){
				#	next;
				#}
				if($name eq "germanonly" and $token ne "German"){
					next;
				}
				if($name eq "dutchonly" and $token ne "Dutch"){
					next;
				}
				print "  with token <$token>\n";

                        	my $M = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/pattern_to_setIDs_en-M1-$alpha-$beta.dat");

				# find and add setID to CFG
				if(exists $M->{$token}){
					my $setID = $M->{$token};
					#print "    setID <$setID>\n";
					$CFG->{linguistic_patterns}->{M1}->{$alpha}->{$beta}->{$token} = $setID;
					$new_linguistic_patterns->{"$token (M1 $alpha $beta)"} = $setID;
					# collect necessary sets of entities
					$CFG->{sets_of_entities}->{$setID . " <$token>"} = &select_entities($S->{$setID});
				
					mkdir "/opt/massive_correlation/data/dbpedia-2015-04/nhops"
						if not -d "/opt/massive_correlation/data/dbpedia-2015-04/nhops";
					open(OUT,">/opt/massive_correlation/data/dbpedia-2015-04/nhops/Canadian100.txt");
					print OUT "<$_>\n" foreach @{$CFG->{sets_of_entities}->{$setID . " <$token>"}};
					close OUT; exit(0);

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
if(exists $CFG->{linguistic_patterns}->{M2}){
	if(
	#	not $name eq "adjonly" and
		not $name eq "canadianonly" and 
		not $name eq "americanonly" and 
	#	not $name eq "professionalonly" and
		not $name eq "greekonly" and
	#	not $name eq "highestonly"
		not $name eq "dutchonly" and
		not $name eq "germanonly"
	){
		foreach my $alpha (keys %{$CFG->{linguistic_patterns}->{M2}}){ print "with alpha <$alpha>\n";
			foreach my $beta (keys %{$CFG->{linguistic_patterns}->{M2}->{$alpha}}){ print " with beta <$beta>\n";
				foreach my $token (keys %{$CFG->{linguistic_patterns}->{M2}->{$alpha}->{$beta}}){
					my $M = retrieve("/opt/massive_correlation/data/EnglishDBpediaAbstracts/pattern_to_setIDs_en-M2-$alpha-$beta.dat");

					# find and add setID to CFG
					if(exists $M->{$token}){
						my $setID = $M->{$token};
						$CFG->{linguistic_patterns}->{M2}->{$alpha}->{$beta}->{$token} = $setID;
						$new_linguistic_patterns->{"$token (M2 $alpha $beta)"} = $setID;
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


$CFG->{linguistic_patterns} = $new_linguistic_patterns;

print Dump $CFG; <STDIN>;
DumpFile("/opt/massive_correlation/data/EnglishDBpediaAbstracts/lamgrapami-$name-$sampleToN-$timestring.yml", $CFG);
store $CFG, "/opt/massive_correlation/data/EnglishDBpediaAbstracts/lamgrapami-$name-$sampleToN-$timestring.dat";
print " > /opt/massive_correlation/data/EnglishDBpediaAbstracts/lamgrapami-$name-$sampleToN-$timestring.dat\n";

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
