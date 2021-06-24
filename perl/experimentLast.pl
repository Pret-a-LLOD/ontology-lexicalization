#!/usr/bin/perl -w
# vi:si:ts=4:sw=4
use strict;
use YAML::Syck qw(LoadFile DumpFile Load Dump);
use IO::Uncompress::Bunzip2 '$Bunzip2Error';
use URL::Encode qw(url_encode_utf8);
use Number::Bytes::Human qw(format_bytes);
use Text::CSV;


# Note 1. The code makes use of shortened class names and shortened entity names.
# Why? then the filenames are shorter.
# However, this requires assumptions about the URIs, which makes the approach specific to DBpedia.


# Note 2. Specific to English

# Note 3. Specific to DBpedia

# Note 4. Parallelization of step 5

# Note 6. For testing, uncomment the return if statements. however, this will cause some warnings because then some data that is expected to exist will not exist

# Note 7. bzip2 deactivated in step x.

# Step 1. find frequent classes
# Step 2. collect triples per entity and per class
# Step 3. find linguistic patterns per class
# Step 4. collect frequent linguistic patterns for each frequent class
# Step 5. create rules

my $BASEDIR = "/home/elahi/a-teanga/dockerTest/ontology-lexicalization/"; 

my $CFG = {
	min_entities_per_class 		=> 100,
	max_entities_per_class		=> 100,
	min_onegram_length              => 4,
	min_pattern_count 		=> 5, 

	min_anchor_count		=> 10,
	min_propertyonegram_length	=> 4,
	min_propertypattern_count	=> 5,
	min_propertystring_length 	=> 5,
	max_propertystring_length 	=> 50, # was 100 before

	min_supA 			=> 2,
	min_supB 			=> 2,
	min_supAB 			=> 2,

	rulepattern => {
        	predict_l_for_s_given_po		=> 1,
		predict_po_for_s_given_l		=> 1,
		predict_localized_l_for_s_given_po	=> 1,
		predict_po_for_s_given_localized_l	=> 1,

		predict_l_for_s_given_p			=> 1,
		predict_p_for_s_given_l         	=> 1,
		predict_localized_l_for_s_given_p       => 1,
                predict_p_for_s_given_localized_l       => 1,

		predict_l_for_s_given_o         	=> 1,
		predict_o_for_s_given_l         	=> 1,
	
		predict_l_for_o_given_sp         	=> 1,
		predict_sp_for_o_given_l         	=> 1,
		predict_localized_l_for_o_given_sp      => 1,
                predict_sp_for_o_given_localized_l      => 1,

		predict_l_for_o_given_s         	=> 1,
		predict_s_for_o_given_l         	=> 1,

		predict_l_for_o_given_p         	=> 1,
		predict_p_for_o_given_l         	=> 1,
		predict_localized_l_for_o_given_p	=> 1,
                predict_p_for_o_given_localized_l	=> 1,
	},

	stop => {},
};

my $num_to_month = {
        1 => "January",
        "01" => "January",
        2 => "February",
        "02" => "February",
        3 => "March",
        "03" => "March",
        4 => "April",
        "04" => "April",
        5 => "May",
        "05" => "May",
        6 => "June",
        "06" => "June",
        7 => "July",
        "07" => "July",
        8 => "August",
        "08" => "August",
        9 => "September",
        "09" => "September",
        10 => "October",
        11 => "November",
        12 => "December",
};

open(DAT,"<$BASEDIR/data-v4/stopwords-en.txt");
while(defined(my $line=<DAT>)){
	next if $line =~ m/\A#/;
	$line =~ s/\n//;
	$CFG->{stopwords}->{$line} = 1;
}
close DAT;


my $folder_length = 4; # length of the name of the subfolder in $BASEDIR/data-v4/data_per_entity/

open(LOG,">>logfile.txt");


# Step 1. find frequent classes
my $frequent_class_to_entities = {};
my $entity_to_frequent_classes = {};
my $frequent_class_to_entities_file = "$BASEDIR/data-v4/frequent_class_to_entities-" . $CFG->{min_entities_per_class} . "-" . $CFG->{max_entities_per_class} . ".yml";
my $entity_to_frequent_classes_file = "$BASEDIR/data-v4/entity_to_frequent_classes-" . $CFG->{min_entities_per_class} . "-" . $CFG->{max_entities_per_class} . ".yml";
my $step1_time_file = "$BASEDIR/data-v4/step1-" . $CFG->{min_entities_per_class} . "-" . $CFG->{max_entities_per_class} . ".time";
if(
	not -s $frequent_class_to_entities_file or
	not -s $entity_to_frequent_classes_file
){

	my $start_time = time();
	my $entities_with_abstract = {};
	my $entities_with_abstract_file = "$BASEDIR/data-v4/entities_with_abstract.yml";
	if(not -s $entities_with_abstract_file){
		print " < $BASEDIR/data-v4/2020.07.01/short-abstracts_lang=en.ttl.bz2 " . format_bytes(-s "$BASEDIR/data-v4/2020.07.01/short-abstracts_lang=en.ttl.bz2") . "\n";
		my $zh = IO::Uncompress::Bunzip2->new(
			"$BASEDIR/data-v4/2020.07.01/short-abstracts_lang=en.ttl.bz2",
			{ AutoClose => 1, Transparent => 1, }
		) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";

		mkdir "$BASEDIR/data-v4/data_per_entity/" if not -d "$BASEDIR/data-v4/data_per_entity/";

		while(my $line=<$zh>){
			my $obj = parse_NT_into_obj($line);
			next if not defined $obj;
			$entities_with_abstract->{$obj->{s}->{value}} = 1;
		}
		DumpFile($entities_with_abstract_file, $entities_with_abstract);
	} else {
		print " < $entities_with_abstract_file " . format_bytes(-s $entities_with_abstract_file) .  "\n";
		$entities_with_abstract = LoadFile($entities_with_abstract_file);
	}

	print " < $BASEDIR/data-v4/2020.12.01/instance-types_lang=en_specific.ttl.bz2 " . (format_bytes(-s "$BASEDIR/data-v4/2020.12.01/instance-types_lang=en_specific.ttl.bz2")) ."\n";
	my $zh = IO::Uncompress::Bunzip2->new(
		"$BASEDIR/data-v4/2020.12.01/instance-types_lang=en_specific.ttl.bz2",
		{ AutoClose => 1, Transparent => 1, }
	) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";

	my $cnt = 0;
	while(my $line=<$zh>){
		$cnt++;
		print "step 1 - $cnt\n" if $cnt % 100000 == 0;
		#last if $cnt > 1000000; # TODO remove
		my $obj = parse_NT_into_obj($line);
		my $c = $obj->{o}->{value};
		next if $c !~ m/\/Actor\Z/; # TODO remove
		$frequent_class_to_entities->{$c}->{$obj->{s}->{value}} = 1 if exists $entities_with_abstract->{$obj->{s}->{value}};
	}
	foreach my $c (keys %{$frequent_class_to_entities}){ print "c $c\n";
		next if $c !~ m/\/Actor\Z/; # TODO remove
		if(scalar keys %{$frequent_class_to_entities->{$c}} < $CFG->{min_entities_per_class}){
			delete $frequent_class_to_entities->{$c};
		} else {
			my $original_number_of_instances = scalar keys %{$frequent_class_to_entities->{$c}};
			if($original_number_of_instances  >= $CFG->{max_entities_per_class}){
				# assumption: max_entities_per_class is much smaller than actual number of entities per class
				my $sample = {};
				my @entities = keys %{$frequent_class_to_entities->{$c}};
				while(scalar keys %{$sample} < $CFG->{max_entities_per_class}){
					my $e = $entities[rand @entities];
					if(not exists $sample->{$e}){
						print "#entities: " . (scalar keys %{$sample}) . "\n";
					}
					$sample->{$e} = 1;
				}
				$frequent_class_to_entities->{$c} = $sample;
				print LOG "reduced number of instances for class $c from $original_number_of_instances to " . $CFG->{max_entities_per_class} . "\n";
			}
		}
	}
	foreach my $c (keys %{$frequent_class_to_entities}){
		foreach my $e (keys %{$frequent_class_to_entities->{$c}}){
			$entity_to_frequent_classes->{$e}->{$c} = 1;
		}
	}
	print " > $frequent_class_to_entities_file\n";
	DumpFile($frequent_class_to_entities_file, $frequent_class_to_entities);
	print " > $entity_to_frequent_classes_file\n";
	DumpFile($entity_to_frequent_classes_file, $entity_to_frequent_classes);

	my $time_diff = time() - $start_time;
	open(DAT,">$step1_time_file");
	print DAT "$time_diff\n";
	close DAT;

} else {
	print " < $frequent_class_to_entities_file " . format_bytes(-s $frequent_class_to_entities_file) .  "\n";
	$frequent_class_to_entities = LoadFile($frequent_class_to_entities_file);
	print " < $entity_to_frequent_classes_file " . format_bytes(-s $entity_to_frequent_classes_file) . "\n";
	$entity_to_frequent_classes = LoadFile($entity_to_frequent_classes_file);
}
print "done with step 1. wait.\n"; #<STDIN>;


# Step 2: collect triples per entity and per class
my $class_to_pos_to_triples = {};
my $entity_to_pos_to_triples = {};
my $step2_finished_file = "$BASEDIR/data-v4/step2-" . join("-", 
	$CFG->{min_entities_per_class}, 
	$CFG->{max_entities_per_class}, 
	$CFG->{min_anchor_count}
) . ".finished";

my $step2_time_file = "$BASEDIR/data-v4/step2-" . join("-",
        $CFG->{min_entities_per_class},
        $CFG->{max_entities_per_class},
        $CFG->{min_anchor_count}) . ".time";

if(not -e $step2_finished_file){

	my $start_time = time();

	mkdir "$BASEDIR/data-v4/data_per_class" if not -d "$BASEDIR/data-v4/data_per_class";
        mkdir "$BASEDIR/data-v4/data_per_entity" if not -d "$BASEDIR/data-v4/data_per_entity";

	my $labels = {};
        my $labels_file = "$BASEDIR/data-v4/2020.12.01/labels_lang=en.ttl.bz2";
        print " < $labels_file " . format_bytes(-s $labels_file) . "\n";
        my $zhl = IO::Uncompress::Bunzip2->new(
                $labels_file,
                { AutoClose => 1, Transparent => 1, }
        ) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";
        my $cnt = 0;
        while(my $line=<$zhl>){
                my $obj = parse_NT_into_obj($line);
                next if not defined $obj;
                $cnt++;
                print " labels $cnt\n" if $cnt % 100000 == 0;
		#last if $cnt > 100000; # TODO remove
                $labels->{$obj->{s}->{value}} = $obj->{o}->{value};
        }
	close $zhl;

        my $anchors = {};
	$cnt = 0;
        my $zha = IO::Uncompress::Bunzip2->new(
                "$BASEDIR/data-v4/2020.12.01/anchor-texts-sorted-counted-reversed.txt.bz2",
                { AutoClose => 1, Transparent => 1, }
        ) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";
        while(my $line=<$zha>){
                if($line =~ m/(\d+) (.*)\t(.*)\n/){
                        my ($count, $URI, $anchor) = ($1, $2, $3);
			$cnt++;
			print " anchors $cnt\n" if $cnt % 100000 == 0;
			#last if $cnt > 100000; # TODO remove
                        last if $count < $CFG->{min_anchor_count};
			if(exists $labels->{$URI} and $labels->{$URI} eq $anchor){
				#print "ignore anchor $anchor\n";
				next;
			}
                        $anchors->{$URI}->{$anchor} = 1;
                }
        }
        close $zha;


	my $added_label_to_entity_file = {};
	my $added_anchors_to_entity_file = {};

	foreach my $file (
                "$BASEDIR/data-v4/2020.11.01/infobox-properties_lang=en.ttl.bz2",
                "$BASEDIR/data-v4/2020.12.01/mappingbased-objects_lang=en.ttl.bz2",
                "$BASEDIR/data-v4/2020.12.01/mappingbased-literals_lang=en.ttl.bz2",
                "$BASEDIR/data-v4/2020.12.01/instance-types_lang=en_specific.ttl.bz2",
        ){
                print " < $file " . format_bytes(-s $file) . "\n";
                my $zh = IO::Uncompress::Bunzip2->new(
                $file,
                        { AutoClose => 1, Transparent => 1, }
                ) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";

		my $cnt = 0;
                while(my $line=<$zh>){
                        $cnt++;
                        print "step 2 - $file - $cnt\n" if $cnt % 100000 == 0;
			#last if $cnt++ > 10000; # TODO remove

                        my $obj = parse_NT_into_obj($line);
                        next if not defined $obj;
			my $S = $obj->{s}->{value};
			my $P = $obj->{p}->{value};
			my $O = $obj->{o}->{value};

			my @subject_classes = ();
			my @object_classes = ();

			if(exists $entity_to_frequent_classes->{$S}){
                                foreach my $c (keys %{$entity_to_frequent_classes->{$S}}){
                                        if(exists $frequent_class_to_entities->{$c}){
						push(@subject_classes, $c);
					}
				}
			}
			if(exists $entity_to_frequent_classes->{$O}){
                                foreach my $c (keys %{$entity_to_frequent_classes->{$O}}){
                                        if(exists $frequent_class_to_entities->{$c}){
                                                push(@object_classes, $c);
                                        }
                                }
                        }
			if(scalar @subject_classes){
				push(@{$entity_to_pos_to_triples->{$S}->{sub}}, $line);
				if($P eq "http://www.w3.org/2000/01/rdf-schema#label"){
                                        $added_label_to_entity_file->{$S}->{$S} = 1;
                                }
				if($obj->{s}->{type} eq "uri" and not exists $added_label_to_entity_file->{$S}->{$S} and exists $labels->{$S}){
                                        push(@{$entity_to_pos_to_triples->{$S}->{sub}},
                                                "<$S> <http://www.w3.org/2000/01/rdf-schema#label> " . $labels->{$S} . " .\n");
                                        $added_label_to_entity_file->{$S}->{$S} = 1;
                                }
				if($obj->{o}->{type} eq "uri" and not exists $added_label_to_entity_file->{$S}->{$O} and exists $labels->{$O}){
					push(@{$entity_to_pos_to_triples->{$S}->{sub}},
                                               	"<$O> <http://www.w3.org/2000/01/rdf-schema#label> " . $labels->{$O} . " .\n");
					$added_label_to_entity_file->{$S}->{$O} = 1;
				}
				if($obj->{s}->{type} eq "uri" and not exists $added_anchors_to_entity_file->{$S}->{$S} and exists $anchors->{$S}){
					foreach my $anchor (keys %{$anchors->{$S}}){
						push(@{$entity_to_pos_to_triples->{$S}->{sub}},
                                                	"<$S> <http://dbpedia.org/ontology/wikiPageWikiLinkText> " . $anchor . " .\n");
					}
					$added_anchors_to_entity_file->{$S}->{$S} = 1;
				}
				if($obj->{o}->{type} eq "uri" and not exists $added_anchors_to_entity_file->{$S}->{$O} and exists $anchors->{$O}){
                                        foreach my $anchor (keys %{$anchors->{$O}}){
                                                push(@{$entity_to_pos_to_triples->{$S}->{sub}},
                                                        "<$O> <http://dbpedia.org/ontology/wikiPageWikiLinkText> " . $anchor . " .\n");
                                        }
                                        $added_anchors_to_entity_file->{$S}->{$O} = 1;
                                }
			}
			if(scalar @object_classes){
                                push(@{$entity_to_pos_to_triples->{$O}->{obj}}, $line);
				if($obj->{s}->{type} eq "uri" and not exists $added_label_to_entity_file->{$O}->{$S} and exists $labels->{$S}){
                                        push(@{$entity_to_pos_to_triples->{$O}->{obj}},
                                                "<$S> <http://www.w3.org/2000/01/rdf-schema#label> " . $labels->{$S} . " .\n");
                                        $added_label_to_entity_file->{$O}->{$S} = 1;
                                }
				if($obj->{s}->{type} eq "uri" and not exists $added_anchors_to_entity_file->{$O}->{$S} and exists $anchors->{$S}){
                                        foreach my $anchor (keys %{$anchors->{$S}}){
                                                push(@{$entity_to_pos_to_triples->{$O}->{sub}},
                                                        "<$S> <http://dbpedia.org/ontology/wikiPageWikiLinkText> " . $anchor . " .\n");
                                        }
                                        $added_anchors_to_entity_file->{$O}->{$S} = 1;
                                }
				if(not exists $added_anchors_to_entity_file->{$O}->{$O} and exists $anchors->{$O}){
                                        foreach my $anchor (keys %{$anchors->{$O}}){
                                                push(@{$entity_to_pos_to_triples->{$O}->{obj}},
                                                        "<$O> <http://dbpedia.org/ontology/wikiPageWikiLinkText> " . $anchor . " .\n");
                                        }
                                        $added_anchors_to_entity_file->{$O}->{$O} = 1;
                                }
                        }
			foreach my $c (@subject_classes){
				push(@{$class_to_pos_to_triples->{$c}->{sub}}, $line);
			}
			foreach my $c (@object_classes){
                                push(@{$class_to_pos_to_triples->{$c}->{obj}}, $line);
                        }

		} # while line

	} # foreach file


	foreach my $e (keys %{$entity_to_pos_to_triples}){
		my $e_enc;
		if($e =~ m/http:\/\/dbpedia.org\/resource\/(.*)/){
			$e_enc = "dbr-" . url_encode_utf8($1);
		} else {
			print "unexpected entity URI namespace <$e>\n";
                        print LOG "STEP 2 - unexpected entity URI namespace <$e>.\n";
			next;
		}
                my $last = substr($e_enc, -$folder_length);
		mkdir "$BASEDIR/data-v4/data_per_entity/$last/" if not -d "$BASEDIR/data-v4/data_per_entity/$last/";
		foreach my $pos (keys %{$entity_to_pos_to_triples->{$e}}){
			# TODO take care of filenames that are too long
			
			my $entitydatafilename = "$BASEDIR/data-v4/data_per_entity/$last/$e_enc-$pos-" . $CFG->{min_anchor_count} . ".ttl";
                        next if -e "$entitydatafilename.bz2" or -e $entitydatafilename;

			#unlink "$entitydatafilename.bz2" if -s "$entitydatafilename.bz2"; # TODO reactivate
			#print " > $entitydatafilename\n";
                        open(DAT,">$entitydatafilename");
                        foreach my $line (@{$entity_to_pos_to_triples->{$e}->{$pos}}){
                                print DAT $line;
                        }
                        close DAT;
			#system("bzip2 $entitydatafilename"); # TODO reactivate later. this seems to slow down a lot
		}
	}

	mkdir "$BASEDIR/data-v4/data_per_class/" if not -d "$BASEDIR/data-v4/data_per_class";
	foreach my $c (keys %{$class_to_pos_to_triples}){
		my $c_enc;
                if($c =~ m/http:\/\/dbpedia.org\/ontology\/(.*)/){
                        $c_enc = "dbo-" . url_encode_utf8($1);
                } else {
                        print "unexpected class URI namespace <$c>\n";
                        print LOG "STEP 2 - unexpected class URI namespace <$c>.\n";
                        next;
                }
                mkdir "$BASEDIR/data-v4/data_per_class/$c_enc/" if not -d "$BASEDIR/data-v4/data_per_class/$c_enc";
		foreach my $pos (qw(sub obj)){
			if(not exists $class_to_pos_to_triples->{$c}->{$pos}){
				print "empty file for class <$c>\n"; #<STDIN>;
				print LOG "STEP 2 - empty file for class <$c> pos <$pos>.\n";
			}
			#print " > $BASEDIR/data-v4/data_per_class/$c_enc/$pos-" . $CFG->{min_entities_per_class} . "-" . $CFG->{max_entities_per_class} . ".ttl\n";
			open(DAT,">$BASEDIR/data-v4/data_per_class/$c_enc/$pos-" .$CFG->{min_entities_per_class} . "-" . $CFG->{max_entities_per_class} . ".ttl");
			foreach my $line (@{$class_to_pos_to_triples->{$c}->{$pos}}){
				print DAT $line;
			}
			close DAT;
			unlink "$BASEDIR/data-v4/data_per_class/$c_enc/$pos-" . $CFG->{min_entities_per_class} . "-" . $CFG->{max_entities_per_class} . ".ttl.bz2"
				if -e "$BASEDIR/data-v4/data_per_class/$c_enc/$pos-" . $CFG->{min_entities_per_class} . "-" . $CFG->{max_entities_per_class} . ".ttl.bz2";
			system("bzip2 $BASEDIR/data-v4/data_per_class/$c_enc/$pos-" . $CFG->{min_entities_per_class} . "-" . $CFG->{max_entities_per_class} . ".ttl");
		}
	}

	system("touch $step2_finished_file");

	my $time_diff = time() - $start_time;
        open(DAT,">$step2_time_file");
        print DAT "$time_diff\n";
        close DAT;

}
print "done with step 2. wait.\n"; #<STDIN>;


# Step 3. create linguistic patterns per entity
my $step3_finished_file = "$BASEDIR/data-v4/step3-" . join("-",
       $CFG->{min_entities_per_class},
       $CFG->{max_entities_per_class},
       $CFG->{min_onegram_length},
       $CFG->{min_anchor_count},
       $CFG->{min_propertyonegram_length},
       $CFG->{min_propertystring_length},
       $CFG->{max_propertystring_length}
) . ".finished";

my $step3_time_file = "$BASEDIR/data-v4/step3-" . join("-",
       $CFG->{min_entities_per_class},
       $CFG->{max_entities_per_class},
       $CFG->{min_onegram_length},
       $CFG->{min_anchor_count},
       $CFG->{min_propertyonegram_length},
       $CFG->{min_propertystring_length},
       $CFG->{max_propertystring_length}
) . ".time";

if(not -e $step3_finished_file){

	# TODO step 3 could be cleaned up a bit
        my $start_time = time();

	print " < $BASEDIR/data-v4/2020.07.01/short-abstracts_lang=en.ttl.bz2 " . format_bytes(-s "$BASEDIR/data-v4/2020.07.01/short-abstracts_lang=en.ttl.bz2") . "\n";
        my $zh = IO::Uncompress::Bunzip2->new(
                "$BASEDIR/data-v4/2020.07.01/short-abstracts_lang=en.ttl.bz2",
                { AutoClose => 1, Transparent => 1, }
        ) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";

        my $cnt = 0;
	while(my $line=<$zh>){
		$cnt++;
		print "step 3 - $cnt\n" if $cnt % 100000 == 0;
		#last if $cnt > 100; # TODO remove
		
		my $obj = parse_NT_into_obj($line);
                next if not defined $obj;

		my $e = $obj->{s}->{value};
		if(exists $entity_to_frequent_classes->{$e}){
			my $e_enc;
                	if($e =~ m/http:\/\/dbpedia.org\/resource\/(.*)/){
                        	$e_enc = "dbr-" . url_encode_utf8($1);
                	} else {
                        	print "unexpected entity URI namespace <$e>\n";
                        	print LOG "STEP 3 - unexpected entity URI namespace <$e>.\n";
                        	next;
                	}

			my $last = substr($e_enc, -$folder_length);
			my $propertypatternfilename = "$BASEDIR/data-v4/data_per_entity/$last/$e_enc-propertypatterns-" .
                        join("-",
                                $CFG->{min_entities_per_class},
                                $CFG->{max_entities_per_class},
                                $CFG->{min_onegram_length},
                                $CFG->{min_anchor_count},
                                $CFG->{min_propertyonegram_length},
                                $CFG->{min_propertystring_length},
                                $CFG->{max_propertystring_length}
                        ) . ".yml";
                        my $propertypatternfilenamelength = length($propertypatternfilename) +4 - length("$BASEDIR/data-v4/data_per_entity/$last/"); # +4: ".bz2"
			if($propertypatternfilenamelength > 255){
                                print LOG "STEP 3 - cannot create file for entity <$e>, propertypatternfilename too long: $propertypatternfilename ($propertypatternfilenamelength).\n";
				next;
			}

			my $patternfilename = "$BASEDIR/data-v4/data_per_entity/$last/$e_enc-patterns-" . join("-",
                                $CFG->{min_entities_per_class},
                                $CFG->{max_entities_per_class},
                                $CFG->{min_onegram_length}) . ".yml";
                        my $patternfilenamelength = length($patternfilename) +4 - length("$BASEDIR/data-v4/data_per_entity/$last/"); # +4: ".bz2"
		
			if($patternfilenamelength > 255){
                                print LOG "STEP 3 - cannot create file for entity <$e>, patternfilename too long: $patternfilename ($patternfilenamelength).\n";
				next;
			}

			#next if -s "$propertypatternfilename.bz2" and -s "$patternfilename.bz2";

                        my $entitysubdatafilename = "$BASEDIR/data-v4/data_per_entity/$last/$e_enc-sub-" . $CFG->{min_anchor_count} . ".ttl";
			my $entityobjdatafilename = "$BASEDIR/data-v4/data_per_entity/$last/$e_enc-obj-" . $CFG->{min_anchor_count} . ".ttl";
			
			#next if not -s "$entitysubdatafilename.bz2" and not -s "$entityobjdatafilename.bz2"; # TODO remove
		
			my $data = { po => {}, ps => {} };
			my $literals = {}; # TODO remove
			my $labels = {};
			my $anchors = {}; # TODO remove
			my $label_to_entities = {};
			my $anchor_to_entities = {};
			my $other_literals = {};

			if(-s "$entitysubdatafilename.bz2" or -s $entitysubdatafilename){ # TODO remove uncompressed processing
				#print " < $entitysubdatafilename.bz2 " . format_bytes(-s "$entitysubdatafilename.bz2") . "\n";
				my $zh2;
				if(-s "$entitysubdatafilename.bz2"){
					$zh2 = IO::Uncompress::Bunzip2->new(
						"$entitysubdatafilename.bz2",
						{ AutoClose => 1, Transparent => 1, }
					);# or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";
				} else {
					open($zh2, "<", $entitysubdatafilename);
				}
				while(defined(my $line2 = <$zh2>)){
					my $obj2 = parse_NT_into_obj($line2);
					next if not defined $obj2;
					if(
						$obj2->{p}->{value} ne "http://www.w3.org/2000/01/rdf-schema#label" and
						$obj2->{p}->{value} ne "http://dbpedia.org/ontology/wikiPageWikiLinkText"
					){
						$data->{po}->{$obj2->{p}->{value}}->{$obj2->{o}->{value}} = 1;

					}
					
					if($obj2->{o}->{type} =~ m/literal/){
						if($obj2->{p}->{value} eq "http://www.w3.org/2000/01/rdf-schema#label"){
							$labels->{$obj2->{s}->{value}} = $obj2->{o}->{value}; # assumption: there is at most one label per entity
							$label_to_entities->{$obj2->{o}->{value}}->{$obj2->{s}->{value}} = 1;
						} elsif($obj2->{p}->{value} eq "http://dbpedia.org/ontology/wikiPageWikiLinkText"){
							$anchors->{$obj2->{s}->{value}}->{$obj2->{o}->{value}} = 1;
							$anchor_to_entities->{$obj2->{o}->{value}}->{$obj2->{s}->{value}} = 1;
						} else {
							$other_literals->{$obj2->{o}->{value}} = 1;
						}
						#$literals->{$obj2->{o}->{value}} = 1;
					}	
				}
			}


			if(-s "$entityobjdatafilename.bz2" or -s $entityobjdatafilename){ # TODO remove processing of uncompressed
				#print " < $entityobjdatafilename.bz2 " . format_bytes(-s "$entityobjdatafilename.bz2") . "\n";
				my $zh3;
				if(-s "$entityobjdatafilename.bz2"){
					$zh3 = IO::Uncompress::Bunzip2->new(
						"$entityobjdatafilename.bz2",
						{ AutoClose => 1, Transparent => 1, }
					);# or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n"; # TODO reactivate die
				} else {
                                        open($zh3, "<", $entityobjdatafilename);
                                }

				while(defined(my $line3 = <$zh3>)){
					my $obj3 = parse_NT_into_obj($line3);
					next if not defined $obj3;
					if(
                                                $obj3->{p}->{value} ne "http://www.w3.org/2000/01/rdf-schema#label" and
                                                $obj3->{p}->{value} ne "http://dbpedia.org/ontology/wikiPageWikiLinkText"
                                        ){
						$data->{ps}->{$obj3->{p}->{value}}->{$obj3->{s}->{value}} = 1;
					}

					if($obj3->{o}->{type} =~ m/literal/){
                                                if($obj3->{p}->{value} eq "http://www.w3.org/2000/01/rdf-schema#label"){
                                                        $labels->{$obj3->{s}->{value}} = $obj3->{o}->{value}; # assumption: there is at most one label per entity
							$label_to_entities->{$obj3->{o}->{value}}->{$obj3->{s}->{value}} = 1;
                                                } elsif($obj3->{p}->{value} eq "http://dbpedia.org/ontology/wikiPageWikiLinkText"){
                                                        $anchors->{$obj3->{s}->{value}}->{$obj3->{o}->{value}} = 1;
							$anchor_to_entities->{$obj3->{o}->{value}}->{$obj3->{s}->{value}} = 1;
                                                } else {
							$other_literals->{$obj3->{o}->{value}} = 1;
						}
						# $literals->{$obj3->{o}->{value}} = 1;
                                        }
				}
			}
			
			#print Dump { e => $e, data => $data }; <STDIN>;
			my $abstract = $obj->{o}->{value};
			$abstract =~ s/\A"//;
        		$abstract =~ s/"\@en\Z//; # TODO language specific
			#print "ABSTRACT <$abstract>\n";
		
			my $entity_label = $labels->{$e};
			$entity_label =~ s/\A"//;
                        $entity_label =~ s/"\@en\Z//; # TODO language specific

			#if(not defined $entity_label){ # TODO do something, e.g., log und skip
			#	print Dump {
			#		entity => $e,
			#		e_enc => $e_enc,
			#		last => $last,
			#		entity_label => $entity_label,
			#		abstract => $abstract,
			#		labels => $labels,
			#		anchors => $anchors,
			#		other_literals => $other_literals,
			#	}; #<STDIN>;
			#}

			#{
			#	# Did not work well here: "The idea of seelling the computer" -> "The Apple I of selling the computer"
			#	my @matches;
			#	push (@matches,$1) while($abstract =~ /\. The (\w+)/g);
			#	foreach my $match (@matches){
			#		next if $entity_label =~ m/\A$match/;
			#		$abstract =~ s/\. The $match/. The $entity_label/;
			#		#print " 1> $abstract\n"; <STDIN>;
			#	}
			#}
			{
				my @matches;
				push (@matches,$1) while($abstract =~ /\. He /g);
				foreach my $match (@matches){
					$abstract =~ s/\. He /. $entity_label /;
					#print " 2> $abstract\n"; <STDIN>;
				}
			}
			{
				my @matches;
				push (@matches,$1) while($abstract =~ /\. She /g);
				foreach my $match (@matches){
					$abstract =~ s/\. She /. $entity_label /;
					#print " 3> $abstract\n"; <STDIN>;
				}
			}
			{
                                my @matches;
                                push (@matches,$1) while($abstract =~ /\. It /g);
                                foreach my $match (@matches){
                                        $abstract =~ s/\. It /. $entity_label /;
					#print " 4> $abstract\n"; <STDIN>;
                                }
                        }
			{
				my @matches;
				push (@matches,$1) while($abstract =~ /\. His /g);
				foreach my $match (@matches){
					$abstract =~ s/\. His /. $entity_label\'s /;
					#print " 5> $abstract\n"; <STDIN>;
				}
			}
			{
				my @matches;
				push (@matches,$1) while($abstract =~ /\. Her /g);
				foreach my $match (@matches){
					$abstract =~ s/\. Her /. $entity_label\'s /;
					#print " 6> $abstract\n"; <STDIN>;
				}
			}
			{
				my @matches;
				push (@matches,$1) while($abstract =~ / he /g);
				foreach my $match (@matches){
					$abstract =~ s/ he / $entity_label /;
					#print " 7> $abstract\n"; <STDIN>;
				}
			}
			{
				my @matches;
				push (@matches,$1) while($abstract =~ / she /g);
				foreach my $match (@matches){
					$abstract =~ s/ she / $entity_label /;
					#print " 8> $abstract\n"; <STDIN>;
				}
			}
			{
                                my @matches;
                                push (@matches,$1) while($abstract =~ /\. Its /g);
                                foreach my $match (@matches){
                                        $abstract =~ s/\. Its /\. ${entity_label}'s /;
					#print " 9> $abstract\n"; <STDIN>;
                                }
                        }

			foreach my $literal (keys %{$other_literals}){
				$literals->{$literal} = 1;
			}
			foreach my $entity (keys %{$anchors}){
				foreach my $anchor (keys %{$anchors->{$entity}}){
					$literals->{$anchor} = 1;
				}
			}
			foreach my $entity (keys %{$labels}){
				$literals->{$labels->{$entity}} = 1;
			}
			my $identifications = &identify($abstract, $literals);

			#print Dump { identifications => $identifications, abstract => $abstract, abstract_original => $obj->{o}->{value} }; <STDIN>;

			my $entity_to_coordinates = {}; # via label and anchor. create from $identifications.
			foreach my $entity (keys %{$labels}){
				my $label = $labels->{$entity};
				if(exists $identifications->{$label}){
					foreach my $coord (keys %{$identifications->{$label}}){
						$entity_to_coordinates->{$entity}->{$coord} = 1;
					}
				}
			}
			foreach my $entity (keys %{$anchors}){
				foreach my $anchor (keys %{$anchors->{$entity}}){
					if(exists $identifications->{$anchor}){
						foreach my $coord (keys %{$identifications->{$anchor}}){
							$entity_to_coordinates->{$entity}->{$coord} = 1;
						}
					}
				}
			}

			#print Dump { entity_to_coordinates => $entity_to_coordinates }; <STDIN>;

			my $property_strings = {};
			if(exists $entity_to_coordinates->{$e}){
				foreach my $p (keys %{$data->{po}}){
					foreach my $o (keys %{$data->{po}->{$p}}){
						if($o !~ m/\A"/ and exists $entity_to_coordinates->{$o}){
							foreach my $coord1 (keys %{$entity_to_coordinates->{$e}}){
								$coord1 =~ m/(\d+)-(\d+)/;
                                                                my ($coord1_start, $coord1_end) = ($1, $2);
								foreach my $coord2 (keys %{$entity_to_coordinates->{$o}}){
									$coord2 =~ m/(\d+)-(\d+)/;
                                                                        my ($coord2_start, $coord2_end) = ($1, $2);
									my $length;
									if($coord1_start < $coord2_start){
                                                                                $length = $coord2_start - $coord1_end;
                                                                                if($length >= $CFG->{min_propertystring_length} and $length  <= $CFG->{max_propertystring_length}){
                                                                                        my $string = substr($abstract, $coord1_end, $length);
											#print "111 <$string> e=$e o=$o p=$p\n"; #<STDIN>;
                                                                                        $property_strings->{$p}->{so}->{$string} = 1;
                                                                                }
                                                                        } else {
                                                                                $length = $coord1_start - $coord2_end;
                                                                                if($length >= $CFG->{min_propertystring_length} and $length <= $CFG->{max_propertystring_length}){
                                                                                        my $string = substr($abstract, $coord2_end, $length);
											#print "222 <$string> e=$e o=$o p=$p\n"; <STDIN>;
                                                                                        $property_strings->{$p}->{os}->{$string} = 1;
                                                                                }
                                                                        }

								}
							}
						} # not literal
						elsif($o =~ m/\A"/){
							foreach my $coord1 (keys %{$identifications->{$labels->{$e}}}){
                                                                $coord1 =~ m/(\d+)-(\d+)/;
                                                                my ($coord1_start, $coord1_end) = ($1, $2);
								foreach my $coord2 (keys %{$identifications->{$o}}){
                                                                        $coord2 =~ m/(\d+)-(\d+)/;
                                                                        my ($coord2_start, $coord2_end) = ($1, $2);
                                                                        my $length;
                                                                        if($coord1_start < $coord2_start){
                                                                                $length = $coord2_start - $coord1_end;
                                                                                if($length >= $CFG->{min_propertystring_length} and $length <= $CFG->{max_propertystring_length}){
                                                                                        my $string = substr($abstract, $coord1_end, $length);
											#print "333 <$string> ($length $coord1_end $coord2_start) $e $o\n";
                                                                                        $property_strings->{$p}->{so}->{$string} = 1;
                                                                                }
                                                                        } else {
                                                                                $length = $coord1_start - $coord2_end;
                                                                                if($length >= $CFG->{min_propertystring_length} and $length <= $CFG->{max_propertystring_length}){
                                                                                        my $string = substr($abstract, $coord2_end, $length);
											#print "444 <$string> ($length $coord2_end $coord1_start) e=$e o=$o p=$p\n"; <STDIN>;
                                                                                        $property_strings->{$p}->{os}->{$string} = 1;
                                                                                }
                                                                        }
                                                                }

							}
						} # literal
					} # foreach o
				} # foreach p (po)

				foreach my $p (keys %{$data->{ps}}){
                                        foreach my $s (keys %{$data->{ps}->{$p}}){
						if(exists $entity_to_coordinates->{$s}){
							foreach my $coord1 (keys %{$entity_to_coordinates->{$e}}){
							        $coord1 =~ m/(\d+)-(\d+)/;
							        my ($coord1_start, $coord1_end) = ($1, $2);
								if(exists $entity_to_coordinates->{$s}){
									foreach my $coord2 (keys %{$entity_to_coordinates->{$s}}){
										$coord2 =~ m/(\d+)-(\d+)/;
										my ($coord2_start, $coord2_end) = ($1, $2);
										my $length;
										if($coord1_start < $coord2_start){
											$length = $coord2_start - $coord1_end;
											if($length >= $CFG->{min_propertystring_length} and $length <= $CFG->{max_propertystring_length}){
												my $string = substr($abstract, $coord1_end, $length);
												#print "555 <$string> ($length $coord1_end $coord2_start) $s $e\n";
												$property_strings->{$p}->{so}->{$string} = 1;
											}
										} else {
											$length = $coord1_start - $coord2_end;
											if($length >= $CFG->{min_propertystring_length} and $length <= $CFG->{max_propertystring_length}){
												my $string = substr($abstract, $coord2_end, $length);
												#print "666 <$string> ($length $coord2_end $coord1_start) e=$s o=$e p=$p\n"; <STDIN>;
												$property_strings->{$p}->{os}->{$string} = 1;
											}
										}
									}
								}
							}
						}
					} # foreach s
				} # foreach p (ps)
			}

			#print Dump { property_strings => $property_strings }; <STDIN>;
			
			my $property_patterns = {};
			foreach my $p (keys %{$property_strings}){
				foreach my $dir (keys %{$property_strings->{$p}}){
					foreach my $string (keys %{$property_strings->{$p}->{$dir}}){
						$property_patterns->{$p}->{$dir}->{exact_string}->{$string} = 1;
						my @onegrams = split(" ", $string);
						foreach my $onegram (@onegrams){
							$onegram =~ s/\.\Z//;
							$onegram =~ s/,\Z//;
							$onegram =~ s/\)\Z//;
							$onegram =~ s/\A\(//;
							$onegram =~ s/:\Z//;

						}
						my $onegrams_h = {};
						foreach my $onegram (@onegrams){
							$property_patterns->{$p}->{$dir}->{onegram}->{$onegram} = 1
							if
							not exists $CFG->{stopwords}->{lc $onegram} and
							length ($onegram) >= $CFG->{min_propertyonegram_length};
						}

						# TODO: could be done more elegantly, max n-gram length could be controlled by a parameter
						for(my $i=0; $i<scalar @onegrams -1; $i++){
							my $cnt_stop = 0;
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i]};
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+1]};
							$property_patterns->{$p}->{$dir}->{twogram}->{join(" ", $onegrams[$i], $onegrams[$i+1])} = 1 if $cnt_stop < 2;
						}
						for(my $i=0; $i<scalar @onegrams -2; $i++){
							my $cnt_stop = 0;
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i]};
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+1]};
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+2]};
							$property_patterns->{$p}->{$dir}->{threegram}->{join(" ", $onegrams[$i], $onegrams[$i+1], $onegrams[$i+2])} = 1 if $cnt_stop < 3;
						}
						for(my $i=0; $i<scalar @onegrams -3; $i++){
							my $cnt_stop = 0;
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i]};
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+1]};
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+2]};
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+3]};
							$property_patterns->{$p}->{$dir}->{fourgram}->{join(" ", $onegrams[$i], $onegrams[$i+1], $onegrams[$i+2], $onegrams[$i+3])} = 1 if $cnt_stop < 4;
						}
						for(my $i=0; $i<scalar @onegrams -4; $i++){
							my $cnt_stop = 0;
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i]};
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+1]};
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+2]};
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+3]};
							$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+4]};
							$property_patterns->{$p}->{$dir}->{fivegram}->{join(" ", $onegrams[$i], $onegrams[$i+1], $onegrams[$i+2], $onegrams[$i+3], $onegrams[$i+4])} = 1 if $cnt_stop < 5;
						}
					}
				}
			}			
			#print Dump { property_patterns => $property_patterns }; <STDIN>;
			#my $propertypatternfilename = "$BASEDIR/data-v4/data_per_entity/$last/$e_enc-propertypatterns-" .
			#join("-",
			#	$CFG->{min_entities_per_class},
			#	$CFG->{max_entities_per_class},
			#	$CFG->{min_onegram_length},
			#	$CFG->{min_anchor_count},
			#	$CFG->{min_propertyonegram_length},
			#	$CFG->{min_propertystring_length},
			#	$CFG->{max_propertystring_length}
			#) . ".yml";
			#my $propertypatternfilenamelength = length($propertypatternfilename) +4 - length("$BASEDIR/data-v4/data_per_entity/$last/"); # +4: ".bz2"
		
			mkdir "$BASEDIR/data-v4/data_per_entity/$last" if not -d "$BASEDIR/data-v4/data_per_entity/$last";
			unlink "$propertypatternfilename.bz2" if -s "$propertypatternfilename.bz2";
                        unlink "$patternfilename.bz2" if -s "$patternfilename.bz2";

			print " > $propertypatternfilename\n";# if rand(100) >= 99;
                        DumpFileCompressed($propertypatternfilename, $property_patterns);

			#my $patternfilename = "$BASEDIR/data-v4/data_per_entity/$last/$e_enc-patterns-" . join("-", 
			# 	$CFG->{min_entities_per_class},
			#	$CFG->{max_entities_per_class},
			#	$CFG->{min_onegram_length}) . ".yml";
			#my $patternfilenamelength = lengt($patternfilename) +4 - length("$BASEDIR/data-v4/data_per_entity/$last/"); # +4: ".bz2"

			if(not -s $patternfilename . ".bz2"){

				# TODO process text on which coreference resolution has been carried out?
				# TODO reduce code duplication
				my $o = $obj->{o}->{value};
				$o =~ s/\A"//;
				$o =~ s/"\@en\Z//;
				my @onegrams = split(" ", $o);
				foreach my $onegram (@onegrams){
					$onegram =~ s/\.\Z//;
					$onegram =~ s/,\Z//;
					$onegram =~ s/\)\Z//;
					$onegram =~ s/\A\(//;
					$onegram =~ s/:\Z//;

				}
				my $onegrams_h = {};
				foreach my $onegram (@onegrams){
					$onegrams_h->{$onegram} = 1
					if
					not exists $CFG->{stopwords}->{lc $onegram} and
					length ($onegram) >= $CFG->{min_onegram_length};
				}
				my $twograms_h;
				for(my $i=0; $i<scalar @onegrams -1; $i++){
					my $cnt_stop = 0;
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i]};
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+1]};
					$twograms_h->{join(" ", $onegrams[$i], $onegrams[$i+1])} = 1 if $cnt_stop < 2;	
				}
				my $threegrams_h;
				for(my $i=0; $i<scalar @onegrams -2; $i++){
					my $cnt_stop = 0;
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i]};
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+1]};
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+2]};
					$threegrams_h->{join(" ", $onegrams[$i], $onegrams[$i+1], $onegrams[$i+2])} = 1 if $cnt_stop < 3;
				}
				my $fourgrams_h;
				for(my $i=0; $i<scalar @onegrams -3; $i++){
					my $cnt_stop = 0;
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i]};
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+1]};
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+2]};
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+3]};
					$fourgrams_h->{join(" ", $onegrams[$i], $onegrams[$i+1], $onegrams[$i+2], $onegrams[$i+3])} = 1 if $cnt_stop < 4;
				}
				my $fivegrams_h;
				for(my $i=0; $i<scalar @onegrams -4; $i++){
					my $cnt_stop = 0;
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i]};
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+1]};
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+2]};
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+3]};
					$cnt_stop++ if exists $CFG->{stopwords}->{lc $onegrams[$i+4]};
					$fivegrams_h->{join(" ", $onegrams[$i], $onegrams[$i+1], $onegrams[$i+2], $onegrams[$i+3], $onegrams[$i+4])} = 1 if $cnt_stop < 5;
				}

				mkdir "$BASEDIR/data-v4/data_per_entity/$last" if not -d "$BASEDIR/data-v4/data_per_entity/$last";
				print " > $patternfilename\n";# if rand(100) >= 99;
				DumpFileCompressed($patternfilename, {
						"1-gram" => [keys %{$onegrams_h}],
						"2-gram" => [keys %{$twograms_h}],
						"3-gram" => [keys %{$threegrams_h}],
						"4-gram" => [keys %{$fourgrams_h}],
						"5-gram" => [keys %{$fivegrams_h}],
					});
			}
		}
        }
        system("touch $step3_finished_file");

	my $time_diff = time() - $start_time;
        open(DAT,">$step3_time_file");
        print DAT "$time_diff\n";
        close DAT;
}
print "done with step 3. wait.\n"; #<STDIN>;


# Step 4. collect frequent linguistic patterns for each frequent class
my $step4_finished_file = "$BASEDIR/data-v4/step4-" . join("-",
	$CFG->{min_entities_per_class},
        $CFG->{max_entities_per_class},
        $CFG->{min_onegram_length},
	$CFG->{min_pattern_count},
        $CFG->{min_anchor_count},
        $CFG->{min_propertyonegram_length},
        $CFG->{min_propertypattern_count},
        $CFG->{min_propertystring_length},
        $CFG->{max_propertystring_length}
) . ".finished";

my $step4_time_file = "$BASEDIR/data-v4/step4-" . join("-",
       $CFG->{min_entities_per_class},
       $CFG->{max_entities_per_class},
       $CFG->{min_onegram_length},
	$CFG->{min_pattern_count},
       $CFG->{min_anchor_count},
       $CFG->{min_propertyonegram_length},
       $CFG->{min_propertypattern_count},
       $CFG->{min_propertystring_length},
       $CFG->{max_propertystring_length}
) . ".time";

if(-e $step3_finished_file and not -e $step4_finished_file){
	
	my $start_time = time();

	foreach my $c (sort keys %{$frequent_class_to_entities}){

		print " " x 0 . " c = $c (step 4)\n";
		my $c_enc;
                if($c =~ m/http:\/\/dbpedia.org\/ontology\/(.*)/){
                        $c_enc = "dbo-" . url_encode_utf8($1);
                } else {
                        print "unexpected class URI namespace <$c>\n";
                        print LOG "STEP 4 - unexpected class URI namespace <$c>.\n";
                        next;
                }
                mkdir "$BASEDIR/data-v4/data_per_class/$c_enc/" if not -d "$BASEDIR/data-v4/data_per_class/$c_enc";

		# create set of linguistic patterns that sufficiently frequently occur with entities of this class

		my $L = {};
		my $LP = {};

		my $classpatternfilename = "$BASEDIR/data-v4/data_per_class/$c_enc/$c_enc-patterns-" . join("-",
			$CFG->{min_entities_per_class},
			$CFG->{max_entities_per_class},
			$CFG->{min_onegram_length},
			$CFG->{min_pattern_count}
		) . ".yml";

		unlink "$classpatternfilename.bz2" if -s "$classpatternfilename.bz2"; # TODO remove
		unlink "$classpatternfilename" if -s "$classpatternfilename"; # TODO remove
		
		next if -e "$classpatternfilename.bz2";

                my $classpropertypatternfilename = "$BASEDIR/data-v4/data_per_class/$c_enc/$c_enc-propertypatterns-" . join("-",
                	$CFG->{min_entities_per_class},
                        $CFG->{max_entities_per_class},
			$CFG->{min_anchor_count},
                        $CFG->{min_propertyonegram_length},
                        $CFG->{min_propertypattern_count},
                        $CFG->{min_propertystring_length},
                        $CFG->{max_propertystring_length}
                ) . ".yml";
		
		unlink "$classpropertypatternfilename.bz2" if -s "$classpropertypatternfilename.bz2"; # TODO remove
		unlink "$classpropertypatternfilename" if -s "$classpropertypatternfilename"; # TODO remove

		if(not -s "$classpatternfilename.bz2" or not -s "$classpropertypatternfilename.bz2"){
			my $cnt_found = 0;
                	my $cnt_missing = 0;
			foreach my $e (keys %{$frequent_class_to_entities->{$c}}){ #print " e $e\n";
				my $e_enc;
                		if($e =~ m/http:\/\/dbpedia.org\/resource\/(.*)/){
                        		$e_enc = "dbr-" . url_encode_utf8($1);
                		} else {
                        		print "unexpected entity URI namespace <$e>\n"; <STDIN>;
                        		print LOG "STEP 4 - unexpected entity URI namespace <$e>.\n";
                        		next;
                		}

                        	my $last = substr($e_enc, -$folder_length);
				my $entitypatternfilename = "$BASEDIR/data-v4/data_per_entity/$last/$e_enc-patterns-" . join("-",
                                	$CFG->{min_entities_per_class},
                                	$CFG->{max_entities_per_class},
                                	$CFG->{min_onegram_length}
				) . ".yml";

				if(-s "$entitypatternfilename.bz2"){ $cnt_found++;
					my $data = LoadFileCompressed($entitypatternfilename);
					foreach my $patterntype (keys %{$data}){
						foreach my $pattern (@{$data->{$patterntype}}){
							$L->{$patterntype}->{$pattern}->{$e} = 1;
						}
					}
				} else {
					$cnt_missing++;
					print LOG "STEP 4 - pattern file missing for entity <$e>, class <$c>. filename: <$entitypatternfilename>.\n";
					print "pattern file does not exist ($c): <$entitypatternfilename>\n"; <STDIN>;
				}

				my $entitypropertypatternfilename = "$BASEDIR/data-v4/data_per_entity/$last/$e_enc-propertypatterns-" . join("-",
						$CFG->{min_entities_per_class},
                                		$CFG->{max_entities_per_class},
                                		$CFG->{min_onegram_length},
                                		$CFG->{min_anchor_count},
                                		$CFG->{min_propertyonegram_length},
                                		$CFG->{min_propertystring_length},
                               			$CFG->{max_propertystring_length}
                        	) . ".yml";

				if(-s "$entitypropertypatternfilename.bz2"){
					$cnt_found++;
					my $data = LoadFileCompressed($entitypropertypatternfilename);
					foreach my $p (keys %{$data}){
						foreach my $dir (keys %{$data->{$p}}){
							foreach my $patterntype (keys %{$data->{$p}->{$dir}}){
								foreach my $pattern (keys %{$data->{$p}->{$dir}->{$patterntype}}){
									$LP->{$p}->{$dir}->{$patterntype}->{$pattern}->{$e} = 1;
								}
							}
						}	
					}
				} else {
					$cnt_missing++;
					print LOG "STEP 4 - propertypattern file missing for entity <$e>, class <$c>. filename: <$entitypropertypatternfilename>.\n";
					print "entitypropertypatternfile does not exist ($c): <$entitypropertypatternfilename>\n";
				}
			}
			foreach my $patterntype (keys %{$L}){
				foreach my $pattern (keys %{$L->{$patterntype}}){
					if(scalar keys %{$L->{$patterntype}->{$pattern}} < $CFG->{min_pattern_count}){
						delete $L->{$patterntype}->{$pattern};
					}
				}
				if(not scalar keys %{$L->{$patterntype}}){
					delete $L->{$patterntype};
				}
			}
			#print "L_red ($c)\n"; <STDIN>; print Dump { c => $c, L_red => $L }; <STDIN>;

			#print Dump { LP => $LP }; <STDIN>;
			foreach my $p (keys %{$LP}){
				foreach my $dir (keys %{$LP->{$p}}){
					foreach my $patterntype (keys %{$LP->{$p}->{$dir}}){
						foreach my $pattern (keys %{$LP->{$p}->{$dir}->{$patterntype}}){
							my $cnt = scalar keys %{$LP->{$p}->{$dir}->{$patterntype}->{$pattern}};
							if($cnt < $CFG->{min_propertypattern_count}){
								delete $LP->{$p}->{$dir}->{$patterntype}->{$pattern};
							}
						}
						delete $LP->{$p}->{$dir}->{$patterntype}
							if not scalar keys %{$LP->{$p}->{$dir}->{$patterntype}};
					}
					delete $LP->{$p}->{$dir}
						if not scalar keys %{$LP->{$p}->{$dir}};
				}
				delete $LP->{$p}
					if not scalar keys %{$LP->{$p}};
			}
			#print "LP_red ($c)\n"; <STDIN>; print Dump { c => $c, LP_red => $LP }; <STDIN>;
			
			print "found $cnt_found, missing $cnt_missing\n";
			print " > $classpropertypatternfilename\n";
			DumpFileCompressed($classpropertypatternfilename, $LP);

			print " > $classpatternfilename\n";
			DumpFileCompressed($classpatternfilename, $L);
		}
	}

	system("touch $step4_finished_file");

	my $time_diff = time() - $start_time;
        open(DAT,">$step4_time_file");
        print DAT "$time_diff\n";
        close DAT;
}
print "done with step 4. wait.\n"; #<STDIN>;


my $bitstring = q{};
foreach my $rulepattern (qw(
	predict_l_for_s_given_po
	predict_po_for_s_given_l
	predict_localized_l_for_s_given_po
	predict_po_for_s_given_localized_l

	predict_l_for_s_given_p
	predict_p_for_s_given_l
	predict_localized_l_for_s_given_p
	predict_p_for_s_given_localized_l

	predict_l_for_s_given_o
	predict_o_for_s_given_l

	predict_l_for_o_given_sp
	predict_sp_for_o_given_l
	predict_localized_l_for_o_given_sp
	predict_sp_for_o_given_localized_l

	predict_l_for_o_given_s
	predict_s_for_o_given_l

	predict_l_for_o_given_p
	predict_p_for_o_given_l
	predict_localized_l_for_o_given_p
	predict_p_for_o_given_localized_l
)){
	$bitstring .= $CFG->{rulepattern}->{$rulepattern};
}

my $step5_finished_file = "$BASEDIR/data-v4/step5-" . join("-",
	$CFG->{min_entities_per_class},
	$CFG->{max_entities_per_class},
	$CFG->{min_onegram_length},
	$CFG->{min_pattern_count},
	$CFG->{min_anchor_count},
	$CFG->{min_propertyonegram_length},
	$CFG->{min_propertypattern_count},
	$CFG->{min_propertystring_length},
	$CFG->{max_propertystring_length},
	$CFG->{min_supA},
	$CFG->{min_supB},
	$CFG->{min_supAB},
	$bitstring
) . ".finished";

my $step5_time_file = "$BASEDIR/data-v4/step5-" . join("-",
	$CFG->{min_entities_per_class},
	$CFG->{max_entities_per_class},
	$CFG->{min_onegram_length},
	$CFG->{min_pattern_count},
	$CFG->{min_anchor_count},
	$CFG->{min_propertyonegram_length},
	$CFG->{min_propertypattern_count},
	$CFG->{min_propertystring_length},
	$CFG->{max_propertystring_length},
	$CFG->{min_supA},
	$CFG->{min_supB},
	$CFG->{min_supAB},
	$bitstring
) . ".time";

if(-e $step4_finished_file and not -e $step5_finished_file){

	my $start_time = time();

	my $csv_header = [
                "class", "ruletype_longname", "ruletype_shortname",
		"linguistic pattern", "order of arguments",
                "patterntype", "subject", "predicate",
                "object", "condAB", "condBA", "supA", "supB",
                "supAB", "AllConf", "Coherence", "Cosine",
                "IR", "Kulczynski", "MaxConf", "string"
	];

	FEC: foreach my $c (sort keys %{$frequent_class_to_entities}){

		my $c_enc;
                if($c =~ m/http:\/\/dbpedia.org\/ontology\/(.*)/){
                        $c_enc = "dbo-" . url_encode_utf8($1);
                } else {
                        print "unexpected class URI namespace <$c>\n";
                        print LOG "STEP 5 - unexpected class URI namespace <$c>.\n";
                        next;
                }

                print "class: $c | $c_enc\n";

		my $parameterstring_nonlocalized = join("-",
                        $c_enc,
                        $CFG->{min_entities_per_class},
                        $CFG->{max_entities_per_class},
                        $CFG->{min_onegram_length},
                        $CFG->{min_pattern_count},
                        $CFG->{min_supA},
                        $CFG->{min_supB},
                        $CFG->{min_supAB}
                );

                my $parameterstring_localized = join("-",
                        $c_enc,
                        $CFG->{min_entities_per_class},
                        $CFG->{max_entities_per_class},
                        $CFG->{min_anchor_count},
                        $CFG->{min_propertyonegram_length},
                        $CFG->{min_propertypattern_count},
                        $CFG->{min_propertystring_length},
                        $CFG->{max_propertystring_length},
                        $CFG->{min_supA},
                        $CFG->{min_supB},
                        $CFG->{min_supAB}
                );

		my $patternfilename = "$BASEDIR/data-v4/data_per_class/$c_enc/$c_enc-patterns-" . join("-",
			$CFG->{min_entities_per_class},
			$CFG->{max_entities_per_class},
			$CFG->{min_onegram_length},
			$CFG->{min_pattern_count}
		) . ".yml";
		my $L = {};
		my $L_rev = {};
		if(
			$CFG->{rulepattern}->{predict_l_for_s_given_po} or
			$CFG->{rulepattern}->{predict_l_for_s_given_p} or
			$CFG->{rulepattern}->{predict_l_for_s_given_o} or
			$CFG->{rulepattern}->{predict_po_for_s_given_l} or
			$CFG->{rulepattern}->{predict_p_for_s_given_l} or
			$CFG->{rulepattern}->{predict_o_for_s_given_l} or
			$CFG->{rulepattern}->{predict_l_for_o_given_sp} or
			$CFG->{rulepattern}->{predict_l_for_o_given_s} or
			$CFG->{rulepattern}->{predict_l_for_o_given_p} or
			$CFG->{rulepattern}->{predict_sp_for_o_given_l} or
			$CFG->{rulepattern}->{predict_s_for_o_given_l} or
			$CFG->{rulepattern}->{predict_p_for_o_given_l}
		){
			print " < $patternfilename.bz2 " . format_bytes(-s "$patternfilename.bz2") . "\n";
			$L = LoadFileCompressed($patternfilename);

			foreach my $patterntype (keys %{$L}){
				foreach my $pattern (keys %{$L->{$patterntype}}){
					foreach my $e (keys %{$L->{$patterntype}->{$pattern}}){
						$L_rev->{$e}->{$patterntype}->{$pattern} = 1; # TODO, maybe, array at the end, not hash
					}
				}
			}
		}

		my $propertypatternfilename = "$BASEDIR/data-v4/data_per_class/$c_enc/$c_enc-propertypatterns-" . join("-",
			$CFG->{min_entities_per_class},
			$CFG->{max_entities_per_class},
			$CFG->{min_anchor_count},
			$CFG->{min_propertyonegram_length},
			$CFG->{min_propertypattern_count},
			$CFG->{min_propertystring_length},
			$CFG->{max_propertystring_length}
		) . ".yml";
		my $LP = {};
		my $LP_rev = {};
		if(
			$CFG->{rulepattern}->{predict_localized_l_for_s_given_po} or
			$CFG->{rulepattern}->{predict_localized_l_for_s_given_p} or
			$CFG->{rulepattern}->{predict_localized_l_for_s_given_o} or
			$CFG->{rulepattern}->{predict_po_for_s_given_localized_l} or
			$CFG->{rulepattern}->{predict_p_for_s_given_localized_l} or
			$CFG->{rulepattern}->{predict_o_for_s_given_localized_l} or
			$CFG->{rulepattern}->{predict_localized_l_for_o_given_sp} or
			$CFG->{rulepattern}->{predict_localized_l_for_o_given_s} or
			$CFG->{rulepattern}->{predict_localized_l_for_o_given_p} or
			$CFG->{rulepattern}->{predict_sp_for_o_given_localized_l} or
			$CFG->{rulepattern}->{predict_s_for_o_given_localized_l} or
			$CFG->{rulepattern}->{predict_p_for_o_given_localized_l}

		){
			print " < $propertypatternfilename.bz2 " . format_bytes(-s "$propertypatternfilename.bz2") . "\n";
			$LP = LoadFileCompressed($propertypatternfilename);

			foreach my $p (keys %{$LP}){
				foreach my $dir (keys %{$LP->{$p}}){
					foreach my $patterntype (keys %{$LP->{$p}->{$dir}}){
						foreach my $pattern (keys %{$LP->{$p}->{$dir}->{$patterntype}}){
							foreach my $e (keys %{$LP->{$p}->{$dir}->{$patterntype}->{$pattern}}){
								$LP_rev->{$e}->{$p}->{$dir}->{$patterntype}->{$pattern} = 1;
							}
						}
					}
				}
			}
		}


		#	  ####   #    #  #####
		#	 #       #    #  #    #
		#	  ####   #    #  #####
		#	      #  #    #  #    #
		#	 #    #  #    #  #    #
		#	  ####    ####   #####

		if(
			$CFG->{rulepattern}->{predict_l_for_s_given_po} or
			$CFG->{rulepattern}->{predict_l_for_s_given_p} or
			$CFG->{rulepattern}->{predict_l_for_s_given_o} or
			$CFG->{rulepattern}->{predict_po_for_s_given_l} or
			$CFG->{rulepattern}->{predict_p_for_s_given_l} or
			$CFG->{rulepattern}->{predict_o_for_s_given_l} or
			$CFG->{rulepattern}->{predict_localized_l_for_s_given_po} or
			$CFG->{rulepattern}->{predict_localized_l_for_s_given_p} or
			$CFG->{rulepattern}->{predict_po_for_s_given_localized_l} or
			$CFG->{rulepattern}->{predict_p_for_s_given_localized_l}
		){
			my $datafilename = "$BASEDIR/data-v4/data_per_class/$c_enc/sub-" . $CFG->{min_entities_per_class} . "-" . $CFG->{max_entities_per_class} . ".ttl";
			next FEC if not -s "$datafilename.bz2";

			my $frequent_terms = {};
			print " < $datafilename.bz2 " . format_bytes(-s "$datafilename.bz2") . "\n";
			my $zh0 = IO::Uncompress::Bunzip2->new(
				"$datafilename.bz2",
				{ AutoClose => 1, Transparent => 1, }
			) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";
			my $cnt = 0;
			while(my $line=<$zh0>){
				print "step5-0 $c sub $cnt\n" if $cnt % 100000 == 0; $cnt++;
				#last if $cnt > 10000; # TODO remove
				my $obj = parse_NT_into_obj($line);
				next if not defined $obj;
				my $s = $obj->{s}->{value};
				my $p = $obj->{p}->{value};
				my $o = $obj->{o}->{value};

				next if
					$o eq $c and
					$p eq "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

				$frequent_terms->{s}->{$s}++;
				$frequent_terms->{p}->{$p}++;
				$frequent_terms->{o}->{$o}++;

				$frequent_terms->{po}->{$p . $o}++;
			}
			close $zh0;

			#my $D = {};
		
			my $I = {};

			my $zh1 = IO::Uncompress::Bunzip2->new(
                                "$datafilename.bz2",
                                { AutoClose => 1, Transparent => 1, }
                        ) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";
                        $cnt = 0;
			while(my $line=<$zh1>){
				print "step5-1 $c sub $cnt\n" if $cnt % 10000 == 0; $cnt++;
				#last if $cnt > 10000; # TODO remove
				my $obj = parse_NT_into_obj($line);
				next if not defined $obj;
				my $s = $obj->{s}->{value};
				my $p = $obj->{p}->{value};
				my $o = $obj->{o}->{value};

				# remove triples that result in truly-uninteresting rules (tautologies) # TODO could just have not added these triples to the class files beforehand
				next if $p eq "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" and $o eq $c;

				if(
                                        (
						$CFG->{rulepattern}->{predict_l_for_s_given_po} and 
						$frequent_terms->{p}->{$p} >= $CFG->{min_supA} and
						$frequent_terms->{o}->{$o} >= $CFG->{min_supA} and
						$frequent_terms->{po}->{$p.$o} >= $CFG->{min_supA}
					) or (
						$CFG->{rulepattern}->{predict_po_for_s_given_l} and 
						$frequent_terms->{p}->{$p} >= $CFG->{min_supB} and
                                                $frequent_terms->{o}->{$o} >= $CFG->{min_supB} and
                                                $frequent_terms->{po}->{$p.$o} >= $CFG->{min_supB}
					) or (
                                                $CFG->{rulepattern}->{predict_localized_l_for_s_given_po} and
                                                $frequent_terms->{p}->{$p} >= $CFG->{min_supA} and
                                                $frequent_terms->{o}->{$o} >= $CFG->{min_supA} and
                                                $frequent_terms->{po}->{$p.$o} >= $CFG->{min_supA}
                                        ) or (
                                                $CFG->{rulepattern}->{predict_po_for_s_given_localized_l} and
                                                $frequent_terms->{p}->{$p} >= $CFG->{min_supB} and
                                                $frequent_terms->{o}->{$o} >= $CFG->{min_supB} and
                                                $frequent_terms->{po}->{$p.$o} >= $CFG->{min_supB}
                                        )

                                ){
					$I->{po}->{$p}->{$o}->{$s} = 1;
					if($CFG->{rulepattern}->{predict_l_for_s_given_po} or $CFG->{rulepattern}->{predict_po_for_s_given_l}){
						if(exists $L_rev->{$s}){
							foreach my $patterntype (keys %{$L_rev->{$s}}){
								foreach my $l (keys %{$L_rev->{$s}->{$patterntype}}){
									$I->{pol}->{$p}->{$o}->{$patterntype}->{$l}->{$s} = 1;
								}
							}
						}
					}
					if($CFG->{rulepattern}->{predict_localized_l_for_s_given_po} or $CFG->{rulepattern}->{predict_po_for_s_given_localized_l}){
						if(exists $LP_rev->{$s} and exists $LP_rev->{$s}->{$p}){
							foreach my $dir (keys %{$LP_rev->{$s}->{$p}}){
								foreach my $patterntype (keys %{$LP_rev->{$s}->{$p}->{$dir}}){
									foreach my $l (keys %{$LP_rev->{$s}->{$p}->{$dir}->{$patterntype}}){
										$I->{poll}->{$p}->{$o}->{$dir}->{$patterntype}->{$l}->{$s} = 1;
									}
								}
							}
						}
					}
                                }

				if(
					($CFG->{rulepattern}->{predict_l_for_s_given_p} and $frequent_terms->{p}->{$p} >= $CFG->{min_supA}) or
					($CFG->{rulepattern}->{predict_p_for_s_given_l} and $frequent_terms->{p}->{$p} >= $CFG->{min_supB}) or
					($CFG->{rulepattern}->{predict_localized_l_for_s_given_p} and $frequent_terms->{p}->{$p} >= $CFG->{min_supA}) or
                                        ($CFG->{rulepattern}->{predict_p_for_s_given_localized_l} and $frequent_terms->{p}->{$p} >= $CFG->{min_supB})
				){
					$I->{p}->{$p}->{$s} = 1;
					# TODO: isn't it sufficient to use $frequent_terms->{p}->{$p}? probably not, as this is not exactly the same
					if($CFG->{rulepattern}->{predict_l_for_s_given_p} or $CFG->{rulepattern}->{predict_p_for_s_given_l}){
						if(exists $L_rev->{$s}){
							foreach my $patterntype (keys %{$L_rev->{$s}}){
								foreach my $l (keys %{$L_rev->{$s}->{$patterntype}}){
									$I->{pl}->{$p}->{$patterntype}->{$l}->{$s} = 1;
								}
							}
						}
					}
					if($CFG->{rulepattern}->{predict_localized_l_for_s_given_p} or $CFG->{rulepattern}->{predict_p_for_s_given_localized_l}){
						if(exists $LP_rev->{$s} and exists $LP_rev->{$s}->{$p}){
                                                	foreach my $dir (keys %{$LP_rev->{$s}->{$p}}){
                                                        	foreach my $patterntype (keys %{$LP_rev->{$s}->{$p}->{$dir}}){
                                                                	foreach my $l (keys %{$LP_rev->{$s}->{$p}->{$dir}->{$patterntype}}){
                                                                        	$I->{pll}->{$p}->{$dir}->{$patterntype}->{$l}->{$s} = 1;
                                                                	}
                                                        	}
                                                	}
                                        	}
					}
				}

				if(
                                        ($CFG->{rulepattern}->{predict_l_for_s_given_o} and $frequent_terms->{o}->{$o} >= $CFG->{min_supA}) or
                                        ($CFG->{rulepattern}->{predict_o_for_s_given_l} and $frequent_terms->{o}->{$o} >= $CFG->{min_supB})
                                ){
					$I->{o}->{$o}->{$s} = 1;
					if($CFG->{rulepattern}->{predict_l_for_s_given_o} or $CFG->{rulepattern}->{predict_o_for_s_given_l}){
						if(exists $L_rev->{$s}){
							foreach my $patterntype (keys %{$L_rev->{$s}}){
								foreach my $l (keys %{$L_rev->{$s}->{$patterntype}}){
									$I->{ol}->{$o}->{$patterntype}->{$l}->{$s} = 1;
								}
							}
						}
					}
                                }
			}
			close $zh1;

			if($CFG->{rulepattern}->{predict_l_for_s_given_po} or $CFG->{rulepattern}->{predict_po_for_s_given_l}){
				print "$c_enc sub predict_l_for_s_given_po and predict_po_for_s_given_l ...\n";
				my $filename1_csv = "$BASEDIR/results-v4/rules-predict_l_for_s_given_po-$parameterstring_nonlocalized.csv";
                                my $filename2_csv = "$BASEDIR/results-v4/rules-predict_po_for_s_given_l-$parameterstring_nonlocalized.csv";
                                my $csv1 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my $csv2 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my ($fh1, $fh2);
                                if($CFG->{rulepattern}->{predict_l_for_s_given_po}){
					unlink $filename1_csv if -s $filename1_csv;
                                        unlink "$filename1_csv.bz2" if -s "$filename1_csv.bz2";
                                        open $fh1, ">:encoding(utf8)", $filename1_csv or die "$filename1_csv: $!";
                                        $csv1->say($fh1, $csv_header);
                                }
                                if($CFG->{rulepattern}->{predict_po_for_s_given_l}){
					unlink $filename2_csv if -s $filename2_csv;
                                        unlink "$filename2_csv.bz2" if -s "$filename2_csv.bz2";
					open $fh2, ">:encoding(utf8)", $filename2_csv or die "$filename2_csv: $!";
                                        $csv2->say($fh2, $csv_header);
                                }
				foreach my $p (keys %{$I->{pol}}){
					foreach my $o (keys %{$I->{pol}->{$p}}){
						foreach my $patterntype (keys %{$I->{pol}->{$p}->{$o}}){
							foreach my $l (keys %{$I->{pol}->{$p}->{$o}->{$patterntype}}){
								my $supAB = scalar keys %{$I->{pol}->{$p}->{$o}->{$patterntype}->{$l}};
								next if $supAB < $CFG->{min_supAB};
								if($CFG->{rulepattern}->{predict_l_for_s_given_po}){
									my $supA = scalar keys %{$I->{po}->{$p}->{$o}};
									if($supA >= $CFG->{min_supA}){
										my $supB = scalar keys %{$L->{$patterntype}->{$l}};
										if($supB >= $CFG->{min_supB}){
											#print "found rule for pattern predict_l_for_s_given_po\n";
											$csv1->say($fh1, &csv_line({
                                                                                		c               	=> $c,
                                                                                		rulepattern_long	=> "predict_l_for_s_given_po",
												rulepattern_short	=> "c_s,po => l_s",
                                                                                		l               	=> $l,
												dir			=> q{},
                                                                                		patterntype     	=> $patterntype,
                                                                                		s               	=> q{},
                                                                                		p               	=> $p,
                                                                                		o               	=> $o,
                                                                                		condAB          	=> $supAB/$supA,
                                                                                		condBA          	=> $supAB/$supB,
                                                                                		supA            	=> $supA,
                                                                                		supB            	=> $supB,
                                                                                		supAB           	=> $supAB
                                                                        		}));
										}
									}
								} # predict_l_for_s_given_po
								if($CFG->{rulepattern}){
									my $supA = scalar keys %{$L->{$patterntype}->{$l}};
									if($supA >= $CFG->{min_supA}){
										my $supB = scalar keys %{$I->{po}->{$p}->{$o}};
										if($supB >= $CFG->{min_supB}){
											#print "found rule for pattern predict_po_for_s_given_l\n";
                                                                                        $csv2->say($fh2, &csv_line({
                                                                                                c               	=> $c,
                                                                                                rulepattern_long     	=> "predict_po_for_s_given_l",
												rulepattern_short	=> "c_s,l_s => po",
                                                                                                l               	=> $l,
												dir			=> q{},
                                                                                                patterntype     	=> $patterntype,
                                                                                                s               	=> q{},
                                                                                                p               	=> $p,
                                                                                                o               	=> $o,
                                                                                                condAB          	=> $supAB/$supA,
                                                                                                condBA          	=> $supAB/$supB,
                                                                                                supA            	=> $supA,
                                                                                                supB            	=> $supB,
                                                                                                supAB           	=> $supAB
                                                                                        }));
                                                                                }
									}
								} # predict_po_for_s_given_l	
							}
						}
					}
				}
				if(defined $fh1){
                                        close $fh1;
                                        system("bzip2 $filename1_csv");
                                        print " > $filename1_csv\n";
                                }
                                if(defined $fh2){
                                        close $fh2;
                                        system("bzip2 $filename2_csv");
                                        print " > $filename2_csv\n";
                                }
			} # predict_l_for_s_given_po and predict_po_for_s_given_l
			
			if($CFG->{rulepattern}->{predict_localized_l_for_s_given_po} or $CFG->{rulepattern}->{predict_po_for_s_given_localized_l}){
                                print "$c_enc sub predict_localized_l_for_s_given_po and predict_po_for_s_given_localized_l ...\n";
                                my $filename1_csv = "$BASEDIR/results-v4/rules-predict_localized_l_for_s_given_po-$parameterstring_localized.csv";
                                my $filename2_csv = "$BASEDIR/results-v4/rules-predict_po_for_s_given_localized_l-$parameterstring_localized.csv";
				my $csv1 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my $csv2 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my ($fh1, $fh2);
                                if($CFG->{rulepattern}->{predict_localized_l_for_s_given_po}){
                                        unlink $filename1_csv if -s $filename1_csv;
					unlink "$filename1_csv.bz2" if -s "$filename1_csv.bz2";
					open $fh1, ">:encoding(utf8)", $filename1_csv or die "$filename1_csv: $!";
                                        $csv1->say($fh1, $csv_header);
                                }
                                if($CFG->{rulepattern}->{predict_po_for_s_given_localized_l}){
					unlink $filename2_csv if -s $filename2_csv;
					unlink "$filename2_csv.bz2" if -s "$filename2_csv.bz2";
                                        open $fh2, ">:encoding(utf8)", $filename2_csv or die "$filename2_csv: $!";
                                        $csv2->say($fh2, $csv_header);
                                }
				foreach my $p (keys %{$I->{poll}}){
					foreach my $o (keys %{$I->{poll}->{$p}}){
						foreach my $dir (keys %{$I->{poll}->{$p}->{$o}}){
							foreach my $patterntype (keys %{$I->{poll}->{$p}->{$o}->{$dir}}){
								foreach my $l (keys %{$I->{poll}->{$p}->{$o}->{$dir}->{$patterntype}}){
									my $supAB = scalar keys %{$I->{poll}->{$p}->{$o}->{$dir}->{$patterntype}->{$l}};
									next if $supAB < $CFG->{min_supAB};
									if($CFG->{rulepattern}->{predict_localized_l_for_s_given_po}){
										my $supA = scalar keys %{$I->{po}->{$p}->{$o}};
										if($supA >= $CFG->{min_supA}){
											my $supB = scalar keys %{$LP->{$p}->{$dir}->{$patterntype}->{$l}};
											if($supB >= $CFG->{min_supB}){
												#print "found rule for pattern predict_localized_l_for_s_given_po\n";
												$csv1->say($fh1, &csv_line({
													c               	=> $c,
													rulepattern_long     	=> "predict_localized_l_for_s_given_po",
													rulepattern_short	=> "c_s,po => ll_s",
													l               	=> $l,
													dir			=> $dir,
													patterntype     	=> $patterntype,
													s               	=> q{},
													p               	=> $p,
													o               	=> $o,
													condAB          	=> $supAB/$supA,
													condBA          	=> $supAB/$supB,
													supA            	=> $supA,
													supB            	=> $supB,
													supAB           	=> $supAB
												}));
											}
										}
									} # predict_localized_l_for_s_given_po
									if($CFG->{rulepattern}->{predict_po_for_s_given_localized_l}){
										my $supA = scalar keys %{$LP->{$p}->{$dir}->{$patterntype}->{$l}};
										if($supA >= $CFG->{min_supA}){
											my $supB = scalar keys %{$I->{po}->{$p}->{$o}};
											if($supB >= $CFG->{min_supB}){
												#print "found rule for pattern predict_po_for_s_given_localized_l\n";
												$csv2->say($fh2, &csv_line({
													c               	=> $c,
													rulepattern_long     	=> "predict_po_for_s_given_localized_l",
													rulepattern_short	=> "c_s,ll_s => po",
													l               	=> $l,
													dir			=> $dir,
													patterntype     	=> $patterntype,
													s               	=> q{},
													p               	=> $p,
													o               	=> $o,
													condAB          	=> $supAB/$supA,
													condBA          	=> $supAB/$supB,
													supA            	=> $supA,
													supB            	=> $supB,
													supAB           	=> $supAB
												}));
											}
										}
									} # predict_po_for_s_given_localized_l    
								}
							}
						}
					}
				}
                                if(defined $fh1){
                                        close $fh1;
                                        system("bzip2 $filename1_csv");
                                        print " > $filename1_csv\n";
                                }
                                if(defined $fh2){
                                        close $fh2;
                                        system("bzip2 $filename2_csv");
                                        print " > $filename2_csv\n";
                                }
                        } # predict_localized_l_for_s_given_po and predict_po_for_s_given_localized_l

			if($CFG->{rulepattern}->{predict_l_for_s_given_p} or $CFG->{rulepattern}->{predict_p_for_s_given_l}){
                                print "$c_enc sub predict_l_for_s_given_p and predict_p_for_s_given_l ...\n";
                                my $filename1_csv = "$BASEDIR/results-v4/rules-predict_l_for_s_given_p-$parameterstring_nonlocalized.csv";
                                my $filename2_csv = "$BASEDIR/results-v4/rules-predict_p_for_s_given_l-$parameterstring_nonlocalized.csv";
                                my $csv1 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my $csv2 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my ($fh1, $fh2);
                                if($CFG->{rulepattern}->{predict_l_for_s_given_p}){
                                        unlink $filename1_csv if -s $filename1_csv;
					unlink "$filename1_csv.bz2" if -s "$filename1_csv.bz2";
                                        open $fh1, ">:encoding(utf8)", $filename1_csv or die "$filename1_csv: $!";
                                        $csv1->say($fh1, $csv_header);
                                }
                                if($CFG->{rulepattern}->{predict_p_for_s_given_l}){
                                        unlink $filename2_csv if -s $filename2_csv;
					unlink "$filename2_csv.bz2" if -s "$filename2_csv.bz2";
                                        open $fh2, ">:encoding(utf8)", $filename2_csv or die "$filename2_csv: $!";
                                        $csv2->say($fh2, $csv_header);
                                }
				foreach my $p (keys %{$I->{pl}}){
					foreach my $patterntype (keys %{$I->{pl}->{$p}}){
						foreach my $l (keys %{$I->{pl}->{$p}->{$patterntype}}){
							my $supAB = scalar keys %{$I->{pl}->{$p}->{$patterntype}->{$l}};
							next if $supAB < $CFG->{min_supAB};
							if($CFG->{rulepattern}->{predict_l_for_s_given_p}){
								my $supA = scalar keys %{$I->{p}->{$p}};
								if($supA >= $CFG->{min_supA}){
									my $supB = scalar keys %{$L->{$patterntype}->{$l}};
									if($supB >= $CFG->{min_supB}){
										#print "found rule for pattern predict_l_for_s_given_p\n";
										$csv1->say($fh1, &csv_line({
											c               	=> $c,
											rulepattern_long     	=> "predict_l_for_s_given_p",
											rulepattern_short	=> "c_s,p => l_s",
											l               	=> $l,
											dir			=> q{},
											patterntype     	=> $patterntype,
											s               	=> q{},
											p               	=> $p,
											o               	=> q{},
											condAB          	=> $supAB/$supA,
											condBA          	=> $supAB/$supB,
											supA            	=> $supA,
											supB            	=> $supB,
											supAB           	=> $supAB
										}));
									}
								}
							} # predict_l_for_s_given_p
							if($CFG->{rulepattern}->{predict_p_for_s_given_l}){
								my $supA = scalar keys %{$L->{$patterntype}->{$l}};
								if($supA >= $CFG->{min_supA}){
									my $supB = scalar keys %{$I->{p}->{$p}};
									if($supB >= $CFG->{min_supB}){
										#print "found rule for pattern predict_p_for_s_given_l\n";
										$csv2->say($fh2, &csv_line({
											c               	=> $c,
											rulepattern_long     	=> "predict_p_for_s_given_l",
											rulepattern_short	=> "c_s,l_s => p",
											l               	=> $l,
											dir			=> q{},
											patterntype     	=> $patterntype,
											s               	=> q{},
											p               	=> $p,
											o               	=> q{},
											condAB          	=> $supAB/$supA,
											condBA          	=> $supAB/$supB,
											supA            	=> $supA,
											supB            	=> $supB,
											supAB           	=> $supAB
										}));
									}
								}
							} # predict_p_for_s_given_l    
						}
					}
				}
                                if(defined $fh1){
                                        close $fh1;
                                        system("bzip2 $filename1_csv");
                                        print " > $filename1_csv\n";
                                }
                                if(defined $fh2){
                                        close $fh2;
                                        system("bzip2 $filename2_csv");
                                        print " > $filename2_csv\n";
                                }
                        } # predict_l_for_s_given_p and predict_p_for_s_given_l

			if($CFG->{rulepattern}->{predict_localized_l_for_s_given_p} or $CFG->{rulepattern}->{predict_p_for_s_given_localized_l}){
                                print "$c_enc sub predict_localized_l_for_s_given_p and predict_p_for_s_given_localized_l ...\n";
                                my $filename1_csv = "$BASEDIR/results-v4/rules-predict_localized_l_for_s_given_p-$parameterstring_localized.csv";
                                my $filename2_csv = "$BASEDIR/results-v4/rules-predict_p_for_s_given_localized_l-$parameterstring_localized.csv";
				my $csv1 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my $csv2 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my ($fh1, $fh2);
                                if($CFG->{rulepattern}->{predict_localized_l_for_s_given_p}){
                                        unlink $filename1_csv if -s $filename1_csv;
					unlink "$filename1_csv.bz2" if -s "$filename1_csv.bz2";
                                        open $fh1, ">:encoding(utf8)", $filename1_csv or die "$filename1_csv: $!";
                                        $csv1->say($fh1, $csv_header);
                                }
                                if($CFG->{rulepattern}->{predict_p_for_s_given_localized_l}){
                                        unlink $filename2_csv if -s $filename2_csv;
					unlink "$filename2_csv.bz2" if -s "$filename2_csv.bz2";
                                        open $fh2, ">:encoding(utf8)", $filename2_csv or die "$filename2_csv: $!";
                                        $csv2->say($fh2, $csv_header);
                                }
				foreach my $p (keys %{$I->{pll}}){
					foreach my $dir (keys %{$I->{pll}->{$p}}){
						foreach my $patterntype (keys %{$I->{pll}->{$p}->{$dir}}){
							foreach my $l (keys %{$I->{pll}->{$p}->{$dir}->{$patterntype}}){
								my $supAB = scalar keys %{$I->{pll}->{$p}->{$dir}->{$patterntype}->{$l}};
								next if $supAB < $CFG->{min_supAB};
								if($CFG->{rulepattern}->{predict_localized_l_for_s_given_p}){
									my $supA = scalar keys %{$I->{p}->{$p}};
									if($supA >= $CFG->{min_supA}){
										my $supB = scalar keys %{$LP->{$p}->{$dir}->{$patterntype}->{$l}};
										if($supB >= $CFG->{min_supB}){
											#print "found rule for pattern predict_localized_l_for_s_given_p\n";
											$csv1->say($fh1, &csv_line({
												c               	=> $c,
												rulepattern_long     	=> "predict_localized_l_for_s_given_p",
												rulepattern_short	=> "c_s,p => ll_s",
												l               	=> $l,
												dir             	=> $dir,
												patterntype     	=> $patterntype,
												s               	=> q{},
												p               	=> $p,
												o               	=> q{},
												condAB          	=> $supAB/$supA,
												condBA          	=> $supAB/$supB,
												supA            	=> $supA,
												supB            	=> $supB,
												supAB           	=> $supAB
											}));
										}
									}
								} # predict_localized_l_for_s_given_p
								if($CFG->{rulepattern}->{predict_p_for_s_given_localized_l}){
									my $supA = scalar keys %{$LP->{$p}->{$dir}->{$patterntype}->{$l}};
									if($supA >= $CFG->{min_supA}){
										my $supB = scalar keys %{$I->{p}->{$p}};
										if($supB >= $CFG->{min_supB}){
											#print "found rule for pattern predict_p_for_s_given_localized_l\n";
											$csv2->say($fh2, &csv_line({
												c               	=> $c,
												rulepattern_long     	=> "predict_p_for_s_given_localized_l",
												rulepattern_short	=> "c_s,ll_s => p",
												l               	=> $l,
												dir             	=> $dir,
												patterntype     	=> $patterntype,
												s               	=> q{},
												p               	=> $p,
												o               	=> q{},
												condAB          	=> $supAB/$supA,
												condBA          	=> $supAB/$supB,
												supA            	=> $supA,
												supB            	=> $supB,
												supAB           	=> $supAB
											}));
										}
									}
								} # predict_p_for_s_given_localized_l
							}
						}
					}
				}
                                if(defined $fh1){
                                        close $fh1;
                                        system("bzip2 $filename1_csv");
                                        print " > $filename1_csv\n";
                                }
                                if(defined $fh2){
                                        close $fh2;
                                        system("bzip2 $filename2_csv");
                                        print " > $filename2_csv\n";
                                }
                        } # predict_localized_l_for_s_given_p and predict_p_for_s_given_localized_l

			if($CFG->{rulepattern}->{predict_l_for_s_given_o} or $CFG->{rulepattern}->{predict_o_for_s_given_l}){
                                print "$c_enc sub predict_l_for_s_given_o and predict_o_for_s_given_l ...\n";
                                my $filename1_csv = "$BASEDIR/results-v4/rules-predict_l_for_s_given_o-$parameterstring_nonlocalized.csv";
                                my $filename2_csv = "$BASEDIR/results-v4/rules-predict_o_for_s_given_l-$parameterstring_nonlocalized.csv";
                                my $csv1 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my $csv2 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my ($fh1, $fh2);
                                if($CFG->{rulepattern}->{predict_l_for_s_given_o}){
                                        unlink $filename1_csv if -s $filename1_csv;
					unlink "$filename1_csv.bz2" if -s "$filename1_csv.bz2";
                                        open $fh1, ">:encoding(utf8)", $filename1_csv or die "$filename1_csv: $!";
                                        $csv1->say($fh1, $csv_header);
                                }
                                if($CFG->{rulepattern}->{predict_o_for_s_given_l}){
                                        unlink $filename2_csv if -s $filename2_csv;
					unlink "$filename2_csv.bz2" if -s "$filename2_csv.bz2";
                                        open $fh2, ">:encoding(utf8)", $filename2_csv or die "$filename2_csv: $!";
                                        $csv2->say($fh2, $csv_header);
                                }
				foreach my $o (keys %{$I->{ol}}){
					foreach my $patterntype (keys %{$I->{ol}->{$o}}){
						foreach my $l (keys %{$I->{ol}->{$o}->{$patterntype}}){
							my $supAB = scalar keys %{$I->{ol}->{$o}->{$patterntype}->{$l}};
							next if $supAB < $CFG->{min_supAB};
							if($CFG->{rulepattern}->{predict_l_for_s_given_o}){
								my $supA = scalar keys %{$I->{o}->{$o}};
								if($supA >= $CFG->{min_supA}){
									my $supB = scalar keys %{$L->{$patterntype}->{$l}};
									if($supB >= $CFG->{min_supB}){
										#print "found rule for pattern predict_l_for_s_given_o\n";
										$csv1->say($fh1, &csv_line({
											c               	=> $c,
											rulepattern_long    	=> "predict_l_for_s_given_o",
											rulepattern_short	=> "c_s,o => l_s",
											l               	=> $l,
											dir			=> q{},
											patterntype     	=> $patterntype,
											s               	=> q{},
											p               	=> q{},
											o               	=> $o,
											condAB          	=> $supAB/$supA,
											condBA          	=> $supAB/$supB,
											supA            	=> $supA,
											supB            	=> $supB,
											supAB           	=> $supAB
										}));
									}
								}
							} # predict_l_for_s_given_o
							if($CFG->{rulepattern}->{predict_o_for_s_given_l}){
								my $supA = scalar keys %{$L->{$patterntype}->{$l}};
								if($supA >= $CFG->{min_supA}){
									my $supB = scalar keys %{$I->{o}->{$o}};
									if($supB >= $CFG->{min_supB}){
										#print "found rule for pattern predict_o_for_s_given_l\n";
										$csv2->say($fh2, &csv_line({
											c               	=> $c,
											rulepattern_long     	=> "predict_o_for_s_given_l",
											rulepattern_short	=> "c_s,l_s => o",
											l               	=> $l,
											dir			=> q{},
											patterntype     	=> $patterntype,
											s               	=> q{},
											p               	=> q{},
											o               	=> $o,
											condAB          	=> $supAB/$supA,
											condBA          	=> $supAB/$supB,
											supA            	=> $supA,
											supB            	=> $supB,
											supAB           	=> $supAB
										}));
									}
								}
							} # predict_o_for_s_given_l    
						}
					}
				}
                                if(defined $fh1){
                                        close $fh1;
                                        system("bzip2 $filename1_csv");
                                        print " > $filename1_csv\n";
                                }
                                if(defined $fh2){
                                        close $fh2;
                                        system("bzip2 $filename2_csv");
                                        print " > $filename2_csv\n";
                                }
			} # predict_l_for_s_given_o and predict_o_for_s_given_l

		} # process sub



		#         ####   #####        #
                #        #    #  #    #       #
                #        #    #  #####        #
                #        #    #  #    #       #
                #        #    #  #    #  #    #
                #         ####   #####    ####

                if(
                        $CFG->{rulepattern}->{predict_l_for_o_given_sp} or
                        $CFG->{rulepattern}->{predict_l_for_o_given_s} or
                        $CFG->{rulepattern}->{predict_l_for_o_given_p} or
                        $CFG->{rulepattern}->{predict_sp_for_o_given_l} or
                        $CFG->{rulepattern}->{predict_s_for_o_given_l} or
                        $CFG->{rulepattern}->{predict_p_for_o_given_l} or
                        $CFG->{rulepattern}->{predict_localized_l_for_o_given_sp} or
			#$CFG->{rulepattern}->{predict_localized_l_for_o_given_s} or
                        $CFG->{rulepattern}->{predict_localized_l_for_o_given_p} or
                        $CFG->{rulepattern}->{predict_sp_for_o_given_localized_l} or
			#$CFG->{rulepattern}->{predict_s_for_o_given_localized_l} or
                        $CFG->{rulepattern}->{predict_p_for_o_given_localized_l}
                ){

                        my $datafilename = "$BASEDIR/data-v4/data_per_class/$c_enc/obj-" . $CFG->{min_entities_per_class} . "-" . $CFG->{max_entities_per_class} . ".ttl";
                        next FEC if not -s "$datafilename.bz2";

                        my $frequent_terms = {};
                        print " < $datafilename.bz2 " . format_bytes(-s "$datafilename.bz2") . "\n";
                        my $zh0 = IO::Uncompress::Bunzip2->new(
                                "$datafilename.bz2",
                                { AutoClose => 1, Transparent => 1, }
                        ) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";
                        my $cnt = 0;
                        while(my $line=<$zh0>){
                                print "step5-0 $c sub $cnt\n" if $cnt % 100000 == 0; $cnt++;
                                #last if $cnt > 10000; # TODO remove
                                my $obj = parse_NT_into_obj($line);
                                next if not defined $obj;
                                my $s = $obj->{s}->{value};
                                my $p = $obj->{p}->{value};
                                my $o = $obj->{o}->{value};

                                $frequent_terms->{s}->{$s}++;
                                $frequent_terms->{p}->{$p}++;
                                $frequent_terms->{o}->{$o}++;

                                $frequent_terms->{sp}->{$s . $p}++;
                        }
                        close $zh0;

			my $I = {};

                        my $zh1 = IO::Uncompress::Bunzip2->new(
                                "$datafilename.bz2",
                                { AutoClose => 1, Transparent => 1, }
                        ) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";
                        $cnt = 0;
                        while(my $line=<$zh1>){
                                print "step5-1 $c sub $cnt\n" if $cnt % 10000 == 0; $cnt++;
				#last if $cnt > 10000; # TODO remove
                                my $obj = parse_NT_into_obj($line);
                                next if not defined $obj;
                                my $s = $obj->{s}->{value};
                                my $p = $obj->{p}->{value};
                                my $o = $obj->{o}->{value};

                                # remove triples that result in truly-uninteresting rules (tautologies) # TODO could just have not added these triples to the class files beforehand
                                next if $p eq "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" and $o eq $c; # TODO really necessary when processing obj?

                                if(
                                        (
                                                $CFG->{rulepattern}->{predict_l_for_o_given_sp} and
                                                $frequent_terms->{s}->{$s} >= $CFG->{min_supA} and
                                                $frequent_terms->{p}->{$p} >= $CFG->{min_supA} and
                                                $frequent_terms->{sp}->{$s.$p} >= $CFG->{min_supA}
                                        ) or (
                                                $CFG->{rulepattern}->{predict_sp_for_o_given_l} and
                                                $frequent_terms->{s}->{$s} >= $CFG->{min_supB} and
                                                $frequent_terms->{p}->{$p} >= $CFG->{min_supB} and
                                                $frequent_terms->{sp}->{$s.$p} >= $CFG->{min_supB}
                                        ) or (
                                                $CFG->{rulepattern}->{predict_localized_l_for_o_given_sp} and
                                                $frequent_terms->{s}->{$s} >= $CFG->{min_supA} and
                                                $frequent_terms->{p}->{$p} >= $CFG->{min_supA} and
                                                $frequent_terms->{sp}->{$s.$p} >= $CFG->{min_supA}
                                        ) or (
                                                $CFG->{rulepattern}->{predict_sp_for_o_given_localized_l} and
                                                $frequent_terms->{s}->{$s} >= $CFG->{min_supB} and
                                                $frequent_terms->{p}->{$p} >= $CFG->{min_supB} and
                                                $frequent_terms->{sp}->{$s.$p} >= $CFG->{min_supB}
                                        )

                                ){
                                        $I->{sp}->{$s}->{$p}->{$o} = 1;
					if($CFG->{rulepattern}->{predict_l_for_o_given_sp} or $CFG->{rulepattern}->{predict_sp_for_o_given_l}){
                                        	if(exists $L_rev->{$o}){
                                        	        foreach my $patterntype (keys %{$L_rev->{$o}}){
                                        	                foreach my $l (keys %{$L_rev->{$o}->{$patterntype}}){
                                        	                        $I->{spl}->{$s}->{$p}->{$patterntype}->{$l}->{$o} = 1;
                                        	                }
                                        	        }
                                        	}
					}
					if($CFG->{rulepattern}->{predict_localized_l_for_o_given_sp} or $CFG->{rulepattern}->{predict_sp_for_o_given_localized_l}){
						if(exists $LP_rev->{$o} and exists $LP_rev->{$o}->{$p}){
							foreach my $dir (keys %{$LP_rev->{$o}->{$p}}){
								foreach my $patterntype (keys %{$LP_rev->{$o}->{$p}->{$dir}}){
									foreach my $l (keys %{$LP_rev->{$o}->{$p}->{$dir}->{$patterntype}}){
										$I->{spll}->{$s}->{$p}->{$dir}->{$patterntype}->{$l}->{$o} = 1;
									}
								}
							}	
						}
					}
                                }

                                if(
                                        ($CFG->{rulepattern}->{predict_l_for_o_given_p} and $frequent_terms->{p}->{$p} >= $CFG->{min_supA}) or
                                        ($CFG->{rulepattern}->{predict_p_for_o_given_l} and $frequent_terms->{p}->{$p} >= $CFG->{min_supB}) or
					($CFG->{rulepattern}->{predict_localized_l_for_o_given_p} and $frequent_terms->{p}->{$p} >= $CFG->{min_supA}) or
                                        ($CFG->{rulepattern}->{predict_p_for_o_given_localized_l} and $frequent_terms->{p}->{$p} >= $CFG->{min_supB})
                                ){
                                        $I->{p}->{$p}->{$o} = 1;
                                        # TODO: isn't it sufficient to use $frequent_terms->{p}->{$p}? probably not, as this is not exactly the same
					if($CFG->{rulepattern}->{predict_l_for_o_given_p} or $CFG->{rulepattern}->{predict_p_for_o_given_l}){
						if(exists $L_rev->{$o}){
							foreach my $patterntype (keys %{$L_rev->{$o}}){
								foreach my $l (keys %{$L_rev->{$o}->{$patterntype}}){
									$I->{pl}->{$p}->{$patterntype}->{$l}->{$o} = 1;
								}
							}
						}
					}
					if($CFG->{rulepattern}->{predict_localized_l_for_o_given_p} or $CFG->{rulepattern}->{predict_p_for_o_given_localized_l}){
						if(exists $LP_rev->{$o} and exists $LP_rev->{$o}->{$p}){
							foreach my $dir (keys %{$LP_rev->{$o}->{$p}}){
								foreach my $patterntype (keys %{$LP_rev->{$o}->{$p}->{$dir}}){
									foreach my $l (keys %{$LP_rev->{$o}->{$p}->{$dir}->{$patterntype}}){
										$I->{pll}->{$p}->{$dir}->{$patterntype}->{$l}->{$o} = 1;
									}
								}
							}
						}
					}
                                }

                                if(
                                        ($CFG->{rulepattern}->{predict_l_for_o_given_s} and $frequent_terms->{s}->{$s} >= $CFG->{min_supA}) or
                                        ($CFG->{rulepattern}->{predict_s_for_o_given_l} and $frequent_terms->{s}->{$s} >= $CFG->{min_supB}) or
					($CFG->{rulepattern}->{predict_localized_l_for_o_given_s} and $frequent_terms->{s}->{$s} >= $CFG->{min_supA}) or
					($CFG->{rulepattern}->{predict_s_for_o_given_localized_l} and $frequent_terms->{s}->{$s} >= $CFG->{min_supB})
                                ){
                                        $I->{s}->{$s}->{$o} = 1;
					if($CFG->{rulepattern}->{predict_l_for_o_given_s} or $CFG->{rulepattern}->{predict_s_for_o_given_l}){
						if(exists $L_rev->{$s}){
							foreach my $patterntype (keys %{$L_rev->{$o}}){
								foreach my $l (keys %{$L_rev->{$o}->{$patterntype}}){
									$I->{sl}->{$s}->{$patterntype}->{$l}->{$o} = 1;
								}
							}
						}
					}
                                }

			}

			if($CFG->{rulepattern}->{predict_l_for_o_given_sp} or $CFG->{rulepattern}->{predict_sp_for_o_given_l}){
                                print "$c_enc sub predict_l_for_o_given_sp and predict_sp_for_o_given_l ...\n";
                                my $filename1_csv = "$BASEDIR/results-v4/rules-predict_l_for_o_given_sp-$parameterstring_nonlocalized.csv";
                                my $filename2_csv = "$BASEDIR/results-v4/rules-predict_sp_for_o_given_l-$parameterstring_nonlocalized.csv";
                                my $csv1 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my $csv2 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my ($fh1, $fh2);
                                if($CFG->{rulepattern}->{predict_l_for_o_given_sp}){
                                        unlink $filename1_csv if -s $filename1_csv;
                                        unlink "$filename1_csv.bz2" if -s "$filename1_csv.bz2";
                                        open $fh1, ">:encoding(utf8)", $filename1_csv or die "$filename1_csv: $!";
                                        $csv1->say($fh1, $csv_header);
                                }
                                if($CFG->{rulepattern}->{predict_sp_for_o_given_l}){
                                        unlink $filename2_csv if -s $filename2_csv;
                                        unlink "$filename2_csv.bz2" if -s "$filename2_csv.bz2";
                                        open $fh2, ">:encoding(utf8)", $filename2_csv or die "$filename2_csv: $!";
                                        $csv2->say($fh2, $csv_header);
                                }
                                foreach my $s (keys %{$I->{spl}}){
                                        foreach my $p (keys %{$I->{spl}->{$s}}){
                                                foreach my $patterntype (keys %{$I->{spl}->{$s}->{$p}}){
                                                        foreach my $l (keys %{$I->{spl}->{$s}->{$p}->{$patterntype}}){
                                                                my $supAB = scalar keys %{$I->{spl}->{$s}->{$p}->{$patterntype}->{$l}};
                                                                next if $supAB < $CFG->{min_supAB};
                                                                if($CFG->{rulepattern}->{predict_l_for_o_given_sp}){
                                                                        my $supA = scalar keys %{$I->{sp}->{$s}->{$p}};
                                                                        if($supA >= $CFG->{min_supA}){
                                                                                my $supB = scalar keys %{$L->{$patterntype}->{$l}};
                                                                                if($supB >= $CFG->{min_supB}){
											#print "found rule for pattern predict_l_for_o_given_sp\n";
                                                                                        $csv1->say($fh1, &csv_line({
                                                                                                c               	=> $c,
                                                                                                rulepattern_long     	=> "predict_l_for_o_given_sp",
												rulepattern_short	=> "c_o,sp => l_o",
                                                                                                l               	=> $l,
												dir 			=> q{},
                                                                                                patterntype     	=> $patterntype,
                                                                                                s               	=> $s,
                                                                                                p               	=> $p,
                                                                                                o               	=> q{},
                                                                                                condAB          	=> $supAB/$supA,
                                                                                                condBA          	=> $supAB/$supB,
                                                                                                supA            	=> $supA,
                                                                                                supB            	=> $supB,
                                                                                                supAB           	=> $supAB
                                                                                        }));
                                                                                }
                                                                        }
                                                                } # predict_l_for_o_given_sp
                                                                if($CFG->{rulepattern}->{predict_sp_for_o_given_l}){
                                                                        my $supA = scalar keys %{$L->{$patterntype}->{$l}};
                                                                        if($supA >= $CFG->{min_supA}){
                                                                                my $supB = scalar keys %{$I->{sp}->{$s}->{$p}};
                                                                                if($supB >= $CFG->{min_supB}){
											#print "found rule for pattern predict_sp_for_o_given_l\n";
                                                                                        $csv2->say($fh2, &csv_line({
                                                                                                c               	=> $c,
                                                                                                rulepattern_long     	=> "predict_sp_for_o_given_l",
												rulepattern_short	=> "c_o,l_o => sp",
                                                                                                l               	=> $l,
												dir 			=> q{},
                                                                                                patterntype     	=> $patterntype,
                                                                                                s               	=> $s,
                                                                                                p               	=> $p,
                                                                                                o               	=> q{},
                                                                                                condAB          	=> $supAB/$supA,
                                                                                                condBA          	=> $supAB/$supB,
                                                                                                supA            	=> $supA,
                                                                                                supB            	=> $supB,
                                                                                                supAB           	=> $supAB
                                                                                        }));
                                                                                }
                                                                        }
                                                                } # predict_sp_for_o_given_l    
                                                        }
                                                }
                                        }
                                }
                                if(defined $fh1){
                                        close $fh1;
                                        system("bzip2 $filename1_csv");
                                        print " > $filename1_csv\n";
                                }
                                if(defined $fh2){
                                        close $fh2;
                                        system("bzip2 $filename2_csv");
                                        print " > $filename2_csv\n";
                                }
                        } # predict_l_for_o_given_sp and predict_sp_for_o_given_l

			if($CFG->{rulepattern}->{predict_localized_l_for_o_given_sp} or $CFG->{rulepattern}->{predict_sp_for_o_given_localized_l}){
                                print "$c_enc sub predict_localized_l_for_o_given_sp and predict_sp_for_o_given_localized_l ...\n";
                                my $filename1_csv = "$BASEDIR/results-v4/rules-predict_localized_l_for_o_given_sp-$parameterstring_localized.csv";
                                my $filename2_csv = "$BASEDIR/results-v4/rules-predict_sp_for_o_given_localized_l-$parameterstring_localized.csv";
                                my $csv1 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my $csv2 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
                                my ($fh1, $fh2);
                                if($CFG->{rulepattern}->{predict_localized_l_for_o_given_sp}){
                                        unlink $filename1_csv if -s $filename1_csv;
                                        unlink "$filename1_csv.bz2" if -s "$filename1_csv.bz2";
                                        open $fh1, ">:encoding(utf8)", $filename1_csv or die "$filename1_csv: $!";
                                        $csv1->say($fh1, $csv_header);
                                }
                                if($CFG->{rulepattern}->{predict_sp_for_o_given_localized_l}){
                                        unlink $filename2_csv if -s $filename2_csv;
                                        unlink "$filename2_csv.bz2" if -s "$filename2_csv.bz2";
                                        open $fh2, ">:encoding(utf8)", $filename2_csv or die "$filename2_csv: $!";
                                        $csv2->say($fh2, $csv_header);
                                }
                                foreach my $s (keys %{$I->{spll}}){
                                        foreach my $p (keys %{$I->{spll}->{$s}}){
                                                foreach my $dir (keys %{$I->{spll}->{$s}->{$p}}){
                                                        foreach my $patterntype (keys %{$I->{spll}->{$s}->{$p}->{$dir}}){
                                                                foreach my $l (keys %{$I->{spll}->{$s}->{$p}->{$dir}->{$patterntype}}){
                                                                        my $supAB = scalar keys %{$I->{spll}->{$s}->{$p}->{$dir}->{$patterntype}->{$l}};
                                                                        next if $supAB < $CFG->{min_supAB};
                                                                        if($CFG->{rulepattern}->{predict_localized_l_for_o_given_sp}){
                                                                                my $supA = scalar keys %{$I->{sp}->{$s}->{$p}};
                                                                                if($supA >= $CFG->{min_supA}){
                                                                                        my $supB = scalar keys %{$LP->{$p}->{$dir}->{$patterntype}->{$l}};
                                                                                        if($supB >= $CFG->{min_supB}){
												#print "found rule for pattern predict_localized_l_for_o_given_sp\n";
                                                                                                $csv1->say($fh1, &csv_line({
                                                                                                        c               	=> $c,
                                                                                                        rulepattern_long     	=> "predict_localized_l_for_o_given_sp",
													rulepattern_short	=> "c_o,sp => ll_o",
                                                                                                        l               	=> $l,
                                                                                                        dir             	=> $dir,
                                                                                                        patterntype     	=> $patterntype,
                                                                                                        s               	=> $s,
                                                                                                        p               	=> $p,
                                                                                                        o               	=> q{},
                                                                                                        condAB          	=> $supAB/$supA,
                                                                                                        condBA          	=> $supAB/$supB,
                                                                                                        supA            	=> $supA,
                                                                                                        supB            	=> $supB,
                                                                                                        supAB           	=> $supAB
                                                                                                }));
                                                                                        }
                                                                                }
                                                                        } # predict_localized_l_for_o_given_sp
                                                                        if($CFG->{rulepattern}->{predict_sp_for_o_given_localized_l}){
                                                                                my $supA = scalar keys %{$LP->{$p}->{$dir}->{$patterntype}->{$l}};
                                                                                if($supA >= $CFG->{min_supA}){
                                                                                        my $supB = scalar keys %{$I->{sp}->{$s}->{$p}};
                                                                                        if($supB >= $CFG->{min_supB}){
												#print "found rule for pattern predict_sp_for_o_given_localized_l\n";
                                                                                                $csv2->say($fh2, &csv_line({
                                                                                                        c               	=> $c,
                                                                                                        rulepattern_long     	=> "predict_sp_for_o_given_localized_l",
													rulepattern_short	=> "c_o,ll_o => sp",
                                                                                                        l               	=> $l,
                                                                                                        dir             	=> $dir,
                                                                                                        patterntype     	=> $patterntype,
                                                                                                        s               	=> $s,
                                                                                                        p               	=> $p,
                                                                                                        o               	=> q{},
                                                                                                        condAB          	=> $supAB/$supA,
                                                                                                        condBA          	=> $supAB/$supB,
                                                                                                        supA            	=> $supA,
                                                                                                        supB            	=> $supB,
                                                                                                        supAB           	=> $supAB
                                                                                                }));
                                                                                        }
                                                                                }
                                                                        } # predict_sp_for_o_given_localized_l
                                                                }
                                                        }
                                                }
                                        }
                                }
                                if(defined $fh1){
                                        close $fh1;
                                        system("bzip2 $filename1_csv");
                                        print " > $filename1_csv\n";
                                }
                                if(defined $fh2){
                                        close $fh2;
                                        system("bzip2 $filename2_csv");
                                        print " > $filename2_csv\n";
                                }
                        } # predict_localized_l_for_o_given_sp and predict_sp_for_o_given_localized_l

			if($CFG->{rulepattern}->{predict_l_for_o_given_s} or $CFG->{rulepattern}->{predict_s_for_o_given_l}){
				print "$c_enc sub predict_l_for_o_given_s and predict_s_for_o_given_l ...\n";
				my $filename1_csv = "$BASEDIR/results-v4/rules-predict_l_for_o_given_s-$parameterstring_nonlocalized.csv";
				my $filename2_csv = "$BASEDIR/results-v4/rules-predict_s_for_o_given_l-$parameterstring_nonlocalized.csv";
				my $csv1 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
				my $csv2 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
				my ($fh1, $fh2);
				if($CFG->{rulepattern}->{predict_l_for_o_given_s}){
					unlink $filename1_csv if -s $filename1_csv;
					unlink "$filename1_csv.bz2" if -s "$filename1_csv.bz2";
					open $fh1, ">:encoding(utf8)", $filename1_csv or die "$filename1_csv: $!";
					$csv1->say($fh1, $csv_header);
				}
				if($CFG->{rulepattern}->{predict_s_for_o_given_l}){
					unlink $filename2_csv if -s $filename2_csv;
					unlink "$filename2_csv.bz2" if -s "$filename2_csv.bz2";
					open $fh2, ">:encoding(utf8)", $filename2_csv or die "$filename2_csv: $!";
					$csv2->say($fh2, $csv_header);
				}
				foreach my $s (keys %{$I->{sl}}){
					foreach my $patterntype (keys %{$I->{sl}->{$s}}){
						foreach my $l (keys %{$I->{sl}->{$s}->{$patterntype}}){
							my $supAB = scalar keys %{$I->{sl}->{$s}->{$patterntype}->{$l}};
							next if $supAB < $CFG->{min_supAB};
							if($CFG->{rulepattern}->{predict_l_for_o_given_s}){
								my $supA = scalar keys %{$I->{s}->{$s}};
								if($supA >= $CFG->{min_supA}){
									my $supB = scalar keys %{$L->{$patterntype}->{$l}};
									if($supB >= $CFG->{min_supB}){
										#print "found rule for pattern predict_l_for_o_given_s\n";
										$csv1->say($fh1, &csv_line({
											c               	=> $c,
											rulepattern_long     	=> "predict_l_for_o_given_s",
											rulepattern_short	=> "c_o,s => l_o",
											l               	=> $l,
											dir			=> q{},
											patterntype     	=> $patterntype,
											s               	=> $s,
											p               	=> q{},
											o               	=> q{},
											condAB          	=> $supAB/$supA,
											condBA          	=> $supAB/$supB,
											supA            	=> $supA,
											supB            	=> $supB,
											supAB           	=> $supAB
										}));
									}
								}
							} # predict_l_for_o_given_s
							if($CFG->{rulepattern}->{predict_s_for_o_given_l}){
								my $supA = scalar keys %{$L->{$patterntype}->{$l}};
								if($supA >= $CFG->{min_supA}){
									my $supB = scalar keys %{$I->{s}->{$s}};
									if($supB >= $CFG->{min_supB}){
										#print "found rule for pattern predict_s_for_o_given_l\n";
										$csv2->say($fh2, &csv_line({
											c               	=> $c,
											rulepattern_long     	=> "predict_s_for_o_given_l",
											rulepattern_short	=> "c_o,l_o => s",
											l               	=> $l,
											dir			=> q{},
											patterntype     	=> $patterntype,
											s               	=> $s,
											p               	=> q{},
											o               	=> q{},
											condAB          	=> $supAB/$supA,
											condBA          	=> $supAB/$supB,
											supA            	=> $supA,
											supB            	=> $supB,
											supAB           	=> $supAB
										}));
									}
								}
							} # predict_s_for_o_given_l
						}
					}
				}
				if(defined $fh1){
					close $fh1;
					system("bzip2 $filename1_csv");
					print " > $filename1_csv\n";
				}
				if(defined $fh2){
					close $fh2;
					system("bzip2 $filename2_csv");
					print " > $filename2_csv\n";
				}
			} # predict_l_for_o_given_s and predict_s_for_o_given_l

			if($CFG->{rulepattern}->{predict_l_for_o_given_p} or $CFG->{rulepattern}->{predict_p_for_o_given_l}){
				print "$c_enc sub predict_l_for_o_given_p and predict_p_for_o_given_l ...\n";
				my $filename1_csv = "$BASEDIR/results-v4/rules-predict_l_for_o_given_p-$parameterstring_nonlocalized.csv";
				my $filename2_csv = "$BASEDIR/results-v4/rules-predict_p_for_o_given_l-$parameterstring_nonlocalized.csv";
				my $csv1 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
				my $csv2 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
				my ($fh1, $fh2);
				if($CFG->{rulepattern}->{predict_l_for_o_given_p}){
					unlink $filename1_csv if -s $filename1_csv;
					unlink "$filename1_csv.bz2" if -s "$filename1_csv.bz2";
					open $fh1, ">:encoding(utf8)", $filename1_csv or die "$filename1_csv: $!";
					$csv1->say($fh1, $csv_header);
				}
				if($CFG->{rulepattern}->{predict_p_for_o_given_l}){
					unlink $filename2_csv if -s $filename2_csv;
					unlink "$filename2_csv.bz2" if -s "$filename2_csv.bz2";
					open $fh2, ">:encoding(utf8)", $filename2_csv or die "$filename2_csv: $!";
					$csv2->say($fh2, $csv_header);
				}
				foreach my $p (keys %{$I->{pl}}){
					foreach my $patterntype (keys %{$I->{pl}->{$p}}){
						foreach my $l (keys %{$I->{pl}->{$p}->{$patterntype}}){
							my $supAB = scalar keys %{$I->{pl}->{$p}->{$patterntype}->{$l}};
							next if $supAB < $CFG->{min_supAB};
							if($CFG->{rulepattern}->{predict_l_for_o_given_p}){
								my $supA = scalar keys %{$I->{p}->{$p}};
								if($supA >= $CFG->{min_supA}){
									my $supB = scalar keys %{$L->{$patterntype}->{$l}};
									if($supB >= $CFG->{min_supB}){
										#print "found rule for pattern predict_l_for_o_given_p\n";
										$csv1->say($fh1, &csv_line({
											c               	=> $c,
											rulepattern_long     	=> "predict_l_for_o_given_p",
											rulepattern_short	=> "c_o,p => l_o",
											l               	=> $l,
											dir			=> q{},
											patterntype     	=> $patterntype,
											s               	=> q{},
											p               	=> $p,
											o               	=> q{},
											condAB          	=> $supAB/$supA,
											condBA          	=> $supAB/$supB,
											supA            	=> $supA,
											supB            	=> $supB,
											supAB           	=> $supAB
										}));
									}
								}
							} # predict_l_for_o_given_p
							if($CFG->{rulepattern}->{predict_p_for_o_given_l}){
								my $supA = scalar keys %{$L->{$patterntype}->{$l}};
								if($supA >= $CFG->{min_supA}){
									my $supB = scalar keys %{$I->{p}->{$p}};
									if($supB >= $CFG->{min_supB}){
										#print "found rule for pattern predict_p_for_o_given_l\n";
										$csv2->say($fh2, &csv_line({
											c               	=> $c,
											rulepattern_long     	=> "predict_p_for_o_given_l",
											rulepattern_short	=> "c_o,l_o => p",
											l               	=> $l,
											dir			=> q{},
											patterntype     	=> $patterntype,
											s               	=> q{},
											p               	=> $p,
											o               	=> q{},
											condAB          	=> $supAB/$supA,
											condBA          	=> $supAB/$supB,
											supA            	=> $supA,
											supB            	=> $supB,
											supAB           	=> $supAB
										}));
									}
								}
							} # predict_p_for_o_given_l    
						}
					}
				}
				if(defined $fh1){
					close $fh1;
					system("bzip2 $filename1_csv");
					print " > $filename1_csv\n";
				}
				if(defined $fh2){
					close $fh2;
					system("bzip2 $filename2_csv");
					print " > $filename2_csv\n";
				}
			} # predict_l_for_o_given_p and predict_p_for_o_given_l

			if($CFG->{rulepattern}->{predict_localized_l_for_o_given_p} or $CFG->{rulepattern}->{predict_p_for_o_given_localized_l}){
				print "$c_enc sub predict_localized_l_for_o_given_p and predict_p_for_o_given_localized_l ...\n";
				my $filename1_csv = "$BASEDIR/results-v4/rules-predict_localized_l_for_o_given_p-$parameterstring_localized.csv";
				my $filename2_csv = "$BASEDIR/results-v4/rules-predict_p_for_o_given_localized_l-$parameterstring_localized.csv";
				my $csv1 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
				my $csv2 = Text::CSV->new ({ binary => 1, auto_diag => 1 });
				my ($fh1, $fh2);
				if($CFG->{rulepattern}->{predict_localized_l_for_o_given_p}){
					unlink $filename1_csv if -s $filename1_csv;
					unlink "$filename1_csv.bz2" if -s "$filename1_csv.bz2";
					open $fh1, ">:encoding(utf8)", $filename1_csv or die "$filename1_csv: $!";
					$csv1->say($fh1, $csv_header);
				}
				if($CFG->{rulepattern}->{predict_p_for_o_given_localized_l}){
					unlink $filename2_csv if -s $filename2_csv;
					unlink "$filename2_csv.bz2" if -s "$filename2_csv.bz2";
					open $fh2, ">:encoding(utf8)", $filename2_csv or die "$filename2_csv: $!";
					$csv2->say($fh2, $csv_header);
				}
				foreach my $p (keys %{$I->{pll}}){
					foreach my $dir (keys %{$I->{pll}->{$p}}){
						foreach my $patterntype (keys %{$I->{pll}->{$p}->{$dir}}){
							foreach my $l (keys %{$I->{pll}->{$p}->{$dir}->{$patterntype}}){
								my $supAB = scalar keys %{$I->{pll}->{$p}->{$dir}->{$patterntype}->{$l}};
								next if $supAB < $CFG->{min_supAB};
								if($CFG->{rulepattern}->{predict_localized_l_for_o_given_p}){
									my $supA = scalar keys %{$I->{p}->{$p}};
									if($supA >= $CFG->{min_supA}){
										my $supB = scalar keys %{$LP->{$p}->{$dir}->{$patterntype}->{$l}};
										if($supB >= $CFG->{min_supB}){
											#print "found rule for pattern predict_localized_l_for_o_given_p\n";
											$csv1->say($fh1, &csv_line({
												c               	=> $c,
												rulepattern_long     	=> "predict_localized_l_for_o_given_p",
												rulepattern_short	=> "c_o,p => ll_o",
												l               	=> $l,
												dir             	=> $dir,
												patterntype     	=> $patterntype,
												s               	=> q{},
												p               	=> $p,
												o               	=> q{},
												condAB          	=> $supAB/$supA,
												condBA          	=> $supAB/$supB,
												supA            	=> $supA,
												supB            	=> $supB,
												supAB           	=> $supAB
											}));
										}
									}
								} # predict_localized_l_for_o_given_p
								if($CFG->{rulepattern}->{predict_p_for_o_given_localized_l}){
									my $supA = scalar keys %{$LP->{$p}->{$dir}->{$patterntype}->{$l}};
									if($supA >= $CFG->{min_supA}){
										my $supB = scalar keys %{$I->{p}->{$p}};
										if($supB >= $CFG->{min_supB}){
											#print "found rule for pattern predict_p_for_o_given_localized_l\n";
											$csv2->say($fh2, &csv_line({
												c               	=> $c,
												rulepattern_long     	=> "predict_p_for_o_given_localized_l",
												rulepattern_short	=> "c_o,ll_o => p",
												l               	=> $l,
												dir             	=> $dir,
												patterntype     	=> $patterntype,
												s               	=> q{},
												p               	=> $p,
												o               	=> q{},
												condAB          	=> $supAB/$supA,
												condBA          	=> $supAB/$supB,
												supA            	=> $supA,
												supB            	=> $supB,
												supAB           	=> $supAB
											}));
										}
									}
								} # predict_p_for_o_given_localized_l
							}
						}
					}
				}
				if(defined $fh1){
					close $fh1;
					system("bzip2 $filename1_csv");
					print " > $filename1_csv\n";
				}
				if(defined $fh2){
					close $fh2;
					system("bzip2 $filename2_csv");
					print " > $filename2_csv\n";
				}
			} # predict_localized_l_for_o_given_p and predict_p_for_o_given_localized_l

		} # process obj

	} # FEC

	system("touch $step5_finished_file");

	my $time_diff = time() - $start_time;
	open(DAT,">$step5_time_file");
	print DAT "$time_diff\n";
	close DAT;
}
print "done with step 5. wait.\n"; #<STDIN>;


print LOG "done.\n";
close LOG;


sub csv_line {
	my $d = shift;
	return [
		$d->{c},
		$d->{rulepattern_long},
		$d->{rulepattern_short},
		$d->{l},
		$d->{dir},
		$d->{patterntype},
		$d->{s},
		$d->{p},
		$d->{o},
		$d->{condAB},
		$d->{condBA},
		$d->{supA},
		$d->{supB},
		$d->{supAB},
		&AllConf($d->{condAB}, $d->{condBA}),
		&Coherence($d->{condAB}, $d->{condBA}),
		&Cosine($d->{condAB}, $d->{condBA}),
		&IR($d->{condAB}, $d->{condBA}),
		&Kulczynski($d->{condAB}, $d->{condBA}),
		&MaxConf($d->{condAB}, $d->{condBA}),
		&rule_to_string($d)
	];
}


sub rule_to_string {
	my $d = shift;
	if($d->{rulepattern_long} eq "predict_po_for_s_given_l"){
                return sprintf "%s in c_e and '%s' in l_e => %s(e, %s, %s) in G",
                        &shorten($d->{c}),
                        $d->{l},
                        q{},
                        &shorten($d->{p}),
                        &shorten($d->{o})
                ;
        } elsif($d->{rulepattern_long} eq "predict_po_for_s_given_localized_l"){
                return sprintf "%s in c_e and '%s' in l_e(c,p,%s) => %s(e, %s, %s) in G",
                        &shorten($d->{c}),
                        $d->{l},
			$d->{dir},
                        q{},
                        &shorten($d->{p}),
                        &shorten($d->{o})
                ;
        }

	elsif($d->{rulepattern_long} eq "predict_o_for_s_given_l"){
                return sprintf "%s in c_e and '%s' in l_e => %s(e, %s, %s) in G",
                        &shorten($d->{c}),
                        $d->{l},
                        q{exists p : },
                        q{p},
                        &shorten($d->{o})
                ;
        } elsif($d->{rulepattern_long} eq "predict_o_for_s_given_localized_l"){
                return sprintf "%s in c_e and '%s' in l_e(c,p,%s) => %s(e, %s, %s) in G",
                        &shorten($d->{c}),
                        $d->{l},
			$d->{dir},
                        q{exists p : },
                        q{p},
                        &shorten($d->{o})
                ;
        }
	
	elsif($d->{rulepattern_long} eq "predict_p_for_s_given_l"){
                return sprintf "%s in c_e and '%s' in l_e => %s(e, %s, %s) in G",
                        &shorten($d->{c}),
                        $d->{l},
                        q{exists o : },
                        &shorten($d->{p}),
                        q{o}
                ;
        } elsif($d->{rulepattern_long} eq "predict_p_for_s_given_localized_l"){
		return sprintf "%s in c_e and '%s' in l_e(c,p,%s) => %s(e, %s, %s) in G",
                        &shorten($d->{c}),
                        $d->{l},
			$d->{dir},
                        q{exists o : },
                        &shorten($d->{p}),
                        q{o}
                ;
        }
       
	elsif($d->{rulepattern_long} eq "predict_l_for_s_given_po"){
                return sprintf "%s in c_e and %s(e, %s, %s) in G => '%s' in l_e",
                        &shorten($d->{c}),
                        q{},
                        &shorten($d->{p}),
                        &shorten($d->{o}),
                        $d->{l}
                ;
        } elsif($d->{rulepattern_long} eq "predict_localized_l_for_s_given_po"){
                return sprintf "%s in c_e and %s(e, %s, %s) in G => '%s' in l_e(c,p,%s)",
                        &shorten($d->{c}),
                        q{},
                        &shorten($d->{p}),
                        &shorten($d->{o}),
                        $d->{l},
			$d->{dir}
                ;
        } 
	
	elsif($d->{rulepattern_long} eq "predict_l_for_s_given_o"){
                return sprintf "%s in c_e and %s(e, %s, %s) in G => '%s' in l_e",
                        &shorten($d->{c}),
                        q{exists p : },
                        q{p},
                        &shorten($d->{o}),
                        $d->{l}
                ;
        } elsif($d->{rulepattern_long} eq "predict_localized_l_for_s_given_o"){
                return sprintf "%s in c_e and %s(e, %s, %s) in G => '%s' in l_e(c,p,%s)",
                        &shorten($d->{c}),
                        q{exists p : },
                        q{p},
                        &shorten($d->{o}),
                        $d->{l},
			$d->{dir}
                ;
        }
	
	
	elsif($d->{rulepattern_long} eq "predict_l_for_s_given_p"){
                return sprintf "%s in c_e and %s(e, %s, %s) in G => '%s' in l_e",
                        &shorten($d->{c}),
                        q{exists o : },
                        &shorten($d->{p}),
                        q{o},
                        $d->{l}
                ;
        } elsif($d->{rulepattern_long} eq "predict_localized_l_for_s_given_p"){ 
                return sprintf "%s in c_e and %s(e, %s, %s) in G => '%s' in l_e(c,p,%s)",
                        &shorten($d->{c}),
                        q{exists o : },
                        &shorten($d->{p}),
                        q{o},
                        $d->{l},
			$d->{dir}
                ;
        } 
	
	
	elsif($d->{rulepattern_long} eq "predict_sp_for_o_given_l"){
                return sprintf "%s in c_e and '%s' in l_e => %s(%s, %s, e) in G",
                        &shorten($d->{c}),
                        $d->{l},
                        q{},
                        &shorten($d->{s}),
                        &shorten($d->{p})
                ;
        } elsif($d->{rulepattern_long} eq "predict_sp_for_o_given_localized_l"){ 
                return sprintf "%s in c_e and '%s' in l_e(c,p,%s) => %s(%s, %s, e) in G",
                        &shorten($d->{c}),
                        $d->{l},
			$d->{dir},
                        q{},
                        &shorten($d->{s}),
                        &shorten($d->{p})
                ;
        }
	
	elsif($d->{rulepattern_long} eq "predict_s_for_o_given_l"){
                return sprintf "%s in c_e and '%s' in l_e => %s(%s, %s, e) in G",
                        &shorten($d->{c}),
                        $d->{l},
                        q{exists p : },
                        &shorten($d->{s}),
                        q{p}
                ;
        } elsif($d->{rulepattern_long} eq "predict_s_for_o_given_localized_l"){ 
                return sprintf "%s in c_e and '%s' in l_e(c,p,%s) => %s(%s, %s, e) in G",
                        &shorten($d->{c}),
                        $d->{l},
			$d->{dir},
                        q{exists p : },
                        &shorten($d->{s}),
                        q{p}
                ;
        }
	
	elsif($d->{rulepattern_long} eq "predict_p_for_o_given_l"){
                return sprintf "%s in c_e and '%s' in l_e => %s(%s, %s, e) in G",
                        &shorten($d->{c}),
                        $d->{l},
                        q{exists s : },
                        q{s},
                        &shorten($d->{p})
                ;
       } elsif($d->{rulepattern_long} eq "predict_p_for_o_given_localized_l"){ 
                return sprintf "%s in c_e and '%s' in l_e(c,p,%s) => %s(%s, %s, e) in G",
                        &shorten($d->{c}),
                        $d->{l},
			$d->{dir},
                        q{exists s : },
                        q{s},
                        &shorten($d->{p})
                ;
       }
       
       elsif($d->{rulepattern_long} eq "predict_l_for_o_given_sp"){
                return sprintf "%s in c_e and %s(%s, %s, e) in G => '%s' in l_e",
                        &shorten($d->{c}),
                        q{},
                     	&shorten($d->{s}),
                        &shorten($d->{p}),
                        $d->{l}
                ;
        } elsif($d->{rulepattern_long} eq "predict_localized_l_for_o_given_sp"){ 
                return sprintf "%s in c_e and %s(%s, %s, e) in G => '%s' in l_e(c,p,%s)",
                        &shorten($d->{c}),
                        q{},
                        &shorten($d->{s}),
                        &shorten($d->{p}),
                        $d->{l},
			$d->{dir}
                ;
        } 
	
	
	elsif($d->{rulepattern_long} eq "predict_l_for_o_given_s"){
               	return sprintf "%s in c_e and %s(%s, %s, e) in G => '%s' in l_e",
                        &shorten($d->{c}),
                        q{exists p : },
                        &shorten($d->{s}),
                        q{p},
                        $d->{l}
                ;
        } elsif($d->{rulepattern_long} eq "predict_localized_l_for_o_given_s"){ 
                return sprintf "%s in c_e and %s(%s, %s, e) in G => '%s' in l_e(c,p,%s)",
                        &shorten($d->{c}),
                        q{exists p : },
                        &shorten($d->{s}),
                        q{p},
                        $d->{l},
			$d->{dir}
                ;
        } 
	
	elsif($d->{rulepattern_long} eq "predict_l_for_o_given_p"){
                return sprintf "%s in c_e and %s(%s, %s, e) in G => '%s' in l_e",
                        &shorten($d->{c}),
                        q{exists s : },
                        q{s},
                        &shorten($d->{p}),
                        $d->{l}
        } elsif($d->{rulepattern_long} eq "predict_localized_l_for_o_given_p"){
                return sprintf "%s in c_e and %s(%s, %s, e) in G => '%s' in l_e(c,p,%s)",
                        &shorten($d->{c}),
                        q{exists s : },
                        q{s},
                        &shorten($d->{p}),
                        $d->{l},
			$d->{dir}
        }

	else {
		print "unexpected rule pattern: " . $d->{rulepattern} . "\n";
	}

	return "string";
}

sub AllConf {
	my ($condAB, $condBA) = @_;
	return $condAB < $condBA ? $condAB : $condBA;
}

sub Coherence {
	my ($condAB, $condBA) = @_;
	return 1/(1/$condAB + 1/$condBA);
}

sub Cosine {
	my ($condAB, $condBA) = @_;
	return sqrt($condAB * $condBA);
}

sub Kulczynski {
	my ($condAB, $condBA) = @_;
	return ($condAB + $condBA)/2;
}

sub MaxConf {
	my ($condAB, $condBA) = @_;
	return $condAB < $condBA ? $condBA : $condAB;
}

sub IR {
	my ($condAB, $condBA) = @_;
	abs($condAB - $condBA) / ($condAB + $condBA - $condAB * $condBA);
}

sub shorten {
	my ($string) = @_;

	my $PREFIXES = {
		"http://www.w3.org/1999/02/22-rdf-syntax-ns#"                   => "rdf",
		"http://www.w3.org/2000/01/rdf-schema#"                         => "rdfs",
		"http://dbpedia.org/resource/"                                  => "dbr",
		"http://dbpedia.org/ontology/"                                  => "dbo",
		"http://dbpedia.org/property/"                                  => "dbp",
		"http://purl.org/dc/elements/1.1/"                              => "dc",
		"http://xmlns.com/foaf/0.1/"                                    => "foaf",
		"http://yago-knowledge.org/resource/"                           => "yago",
		"http://www.w3.org/2004/02/skos/core#"                          => "skos",
		"http://schema.org/"                                            => "schema",
		"http://www.wikidata.org/prop/direct-normalized/"               => "wdtn",
		"http://www.wikidata.org/entity/"                               => "wd",
		"http://www.wikidata.org/entity/statement/"                     => "wds",
		"http://www.wikidata.org/value/"                                => "wdv",
		"http://www.wikidata.org/prop/direct/"                          => "wdt",
		"http://wikiba.se/ontology#"                                    => "wikibase",
		"http://www.wikidata.org/prop/"                                 => "wp",
		"http://www.wikidata.org/prop/statement/"                       => "wps",
		"http://www.wikidata.org/prop/qualifier/"                       => "wpq",
		"http://www.w3.org/2002/07/owl#"                                => "owl",
		"http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#"        => "odp",
		"http://www.w3.org/2001/XMLSchema#"                             => "xsd",
		"http://www.w3.org/ns/prov#"                                    => "prov",
		"http://psink.de/scio/"                                         => "scio",
		"http://scio/data/"                                             => "sciodata",
		"http://www.example.org/"                                       => "ex",
		"http://www.georss.org/georss/"                                 => "georss",
		"http://www.w3.org/2003/01/geo/"                                => "wgs84",
	};
	#print "shorten: <$string>\n";

	# TODO shorten datatypes in literals
	foreach my $ns (sort keys %{$PREFIXES}){
		my $ns_qm = quotemeta $ns;
		if($string =~ m/\A$ns_qm(.*)/){
			$string = $PREFIXES->{$ns} . ":$1";
			last;
		}
	}

	#print " -> $string\n";
	return $string;
}


# TODO: this method is not prepared for all kinds of situations, but it was sufficient for the DBpedia data we used
sub parse_NT_into_obj {
        my $string = shift;

	return undef if $string =~ m/\A#/;

        # URI URI URI
        # URI URI LIT-LANG
        # URI URI LIT-DAT
        # URI URI BNODE

        # BNODE URI URI
        # BNODE URI LIT-LANG
        # BNODE URI LIT-DAT
        # BNODE URI BNODE

        # URI URI URI
        if($string =~ m/<(.+)>(?:\s|\t)<(.+)>(?:\s|\t)<(.+)> .\n\Z/){
                return {
                        s => { type => "uri", value => "$1" },
                        p => { type => "uri", value => "$2" },
                        o => { type => "uri", value => "$3" },
                };
        }

        # URI URI LIT-LANG
        elsif($string =~ m/<(.+)>(?:\s|\t)<(.+)>(?:\s|\t)\"(.*)\"\@(.+) .\n\Z/){
                return {
                        s => { type => "uri", value => "$1" },
                        p => { type => "uri", value => "$2" },
                        o => { type => "literal", value => "\"$3\"\@" . $4 },
                };
        }

        # URI URI LIT-DAT
        elsif($string =~ m/<(.+)>(?:\s|\t)<(.+)>(?:\s|\t)\"(.+)\"\^\^<(.*)> .\n\Z/){
                return {
                        s => { type => "uri", value => "$1" },
                        p => { type => "uri", value => "$2" },
                        o => { type => "typed-literal", value => "\"$3\"\^\^<$4>" },
                };
        }

        # URI URI LIT
        elsif($string =~ m/<(.+)>(?:\s|\t)<(.+)>(?:\s|\t)\"(.*)\" .\n\Z/){
                return {
                        s => { type => "uri", value => "$1" },
                        p => { type => "uri", value => "$2" },
                        o => { type => "literal", value => "\"$3\"" },
                };
        }

        else {
                print "Cannot handle line format: <$string>\n"; #<STDIN>;
                print LOG "parse_NT_into_obj - cannot handle line format <$string>.\n";
		return undef; # { s => {}, p => {}, o => {}};
        }
}

sub DumpFileCompressed {
	my ($filename, $data) = @_;
	DumpFile($filename, $data);
	system("bzip2 $filename");
}

sub LoadFileCompressed {
	my $filename = shift;
	my $data = q{};
	#print " < $filename.bz2 " . format_bytes(-s "$filename.bz2") . "\n";
	my $zh = IO::Uncompress::Bunzip2->new(
		"$filename.bz2",  
		{ AutoClose => 1, Transparent => 1, }
	) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";
	while(my $line=<$zh>){
		$data .= $line;
	}
	return Load($data);
}


sub identify {
	my ($text, $literals) = @_;

	#print Dump { sub_identify => {text => $text, literals => $literals}}; <STDIN>;

	my $R = {};

	foreach my $literal (sort keys %{$literals}){
		next if $literal eq "\"\"\@en";

		if($literal =~ m/\A"(.*)"\@en\Z/ or $literal =~ m/\A"(.*)"\Z/){
			next if	not length $literal >= 8;
			my $string = quotemeta $1;

			while ($text =~ /($string)/g) {
 				my $sub_seq = $1;
   				my ($start, $end, $len) = ($-[1], $+[1], $+[1]-$-[1]);
				my $hitpos = "$start-$end";
				$R->{$literal}->{$hitpos} = 1;
   			}
		} elsif($literal =~ m/\A"(.+)"\^\^<http:\/\/www.w3.org\/2001\/XMLSchema#date>\Z/){
			my $string = $1;

			if($string =~ m/(\d\d\d\d?)-(\d\d)-(\d\d)/){
				my ($y, $m, $d) = ($1, $2, $3);

				my $strings = {};
				if(exists $num_to_month->{$m}){
					my $d2 = $d;
					$d2 =~ s/\A0?//;
					$strings->{quotemeta($num_to_month->{$m} . " $d2, $y")} = 1;
					$strings->{quotemeta($d2 . " " . $num_to_month->{$m} . " $y")} = 1;
				}

				foreach my $string (keys %{$strings}){
					while ($text =~ /($string)/g) {
						my $sub_seq = $1;
						my ($start, $end, $len) = ($-[1], $+[1], $+[1]-$-[1]);
						my $hitpos = "$start-$end";
						$R->{$literal}->{$hitpos} = 1;
					}
				}

				if($text =~ m/\D$y\D/){
					while ($text =~ /\D($y)\D/g) {
                                                my $sub_seq = $1;
                                                my ($start, $end, $len) = ($-[1], $+[1], $+[1]-$-[1]);
                                                my $hitpos = "$start-$end";
                                                $R->{$literal}->{$hitpos} = 1;
                                        }
				}
			} else {
				print "unexpected date format: <$string>\n";
			}
		}
		elsif($literal =~ m/\A"(.+)"\^\^<http:\/\/www.w3.org\/2001\/XMLSchema#gMonthDay>\Z/){
                        my $string = $1;

                        if($string =~ m/--(\d\d)-(\d\d)/){
                                my ($m, $d) = ($1, $2);

                                my $strings = {};
                                if(exists $num_to_month->{$m}){
                                        my $d2 = $d;
                                        $d2 =~ s/\A0?//;
                                        $strings->{quotemeta($d2 . " " . $num_to_month->{$m})} = 1;
                                }

				foreach my $string (keys %{$strings}){
					while ($text =~ /($string)/g) {
						my $sub_seq = $1;
						my ($start, $end, $len) = ($-[1], $+[1], $+[1]-$-[1]);
						my $hitpos = "$start-$end";
						$R->{$literal}->{$hitpos} = 1;
					}
				}
                        } else {
                                print "unexpected date format: <$string>\n";
                        }
                }
		elsif(
			$literal =~ m/\A"(.*)\"\^\^<http:\/\/www.w3.org\/2001\/XMLSchema#integer>\Z/
			or $literal =~ m/\A"(.*)\"\^\^<http:\/\/dbpedia.org\/datatype\/Currency>\Z/
                        or $literal =~ m/\A"(.*)\"\^\^<http:\/\/dbpedia.org\/datatype\/usDollar>\Z/
			or $literal =~ m/\A"(.*)\"\^\^<http:\/\/dbpedia.org\/datatype\/perCent>\Z/
			or $literal =~ m/\A"(.*)\"\^\^<http:\/\/dbpedia.org\/datatype\/second>\Z/
			or $literal =~ m/\A"(.*)\"\^\^<http:\/\/dbpedia.org\/datatype\/rod>\Z/
			#or $literal =~ m/\A"(.*)\"\^\^<http:\/\/dbpedia.org\/datatype\/Currency>\Z/

			or $literal =~ m/\A"(.*)\"\^\^<http:\/\/www.w3.org\/2001\/XMLSchema#gYear>\Z/
			or $literal =~ m/\A"(.*)\"\^\^<http:\/\/www.w3.org\/2001\/XMLSchema#integer>\Z/
			or $literal =~ m/\A"(.*)\"\^\^<http:\/\/www.w3.org\/2001\/XMLSchema#nonNegativeInteger>\Z/
                        or $literal =~ m/\A"(.*)\"\^\^<http:\/\/www.w3.org\/2001\/XMLSchema#positiveInteger>\Z/
			or $literal =~ m/\A"(.*)\"\^\^<http:\/\/www.w3.org\/2001\/XMLSchema#double>\Z/
                        or $literal =~ m/\A"(.*)\"\^\^<http:\/\/www.w3.org\/2001\/XMLSchema#float>\Z/

		){
			my $string = $1;
			while ($text =~ /($string)/g) {
                                my $sub_seq = $1;
                                my ($start, $end, $len) = ($-[1], $+[1], $+[1]-$-[1]);
                                my $hitpos = "$start-$end";
                                $R->{$literal}->{$hitpos} = 1;
                        }
		}
		else {
			# ignore for now. # TODO: for any unknown datatype, try to match value
			print "unexpected literal: <$literal>\n"; #<STDIN>;
		}
	}
	
	return $R;
}

