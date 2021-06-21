#!/usr/bin/perl -w
use strict;
use IO::Uncompress::Bunzip2 '$Bunzip2Error';
use Text::CSV;
use YAML::Syck qw(Dump DumpFile);
use Clone qw(clone);
use URL::Encode qw(url_encode_utf8);


my $CFG = {
        min_entities_per_class          => 100,
        max_entities_per_class          => 10000,
        min_onegram_length              => 4,
        min_pattern_count               => 5,

        min_anchor_count                => 10,
        min_propertyonegram_length      => 4,
        min_propertypattern_count       => 5,
        min_propertystring_length       => 5,
        max_propertystring_length       => 50,

	# the support threshold values used when the data was created (condBA, condAB are 0)
	orig_min_supA                   => 5,
	orig_min_supB                   => 5,
	orig_min_supAB                  => 5,

	# the support threshold values etc. to be used when creating the statistics (counting)
	counting_min_supA               => 5,
        counting_min_supB               => 5,
        counting_min_supAB              => 5,
	counting_min_condBA		=> 0,
	counting_min_condAB		=> 0,

	# the support threshold values etc. for those rules that the examples are taken from (collecting)
        collecting_min_supA             => 15,
        collecting_min_supB             => 15,
        collecting_min_supAB            => 15,
	collecting_min_supAB            => 10,
        collecting_min_condBA           => 0.1,
        collecting_min_condAB           => 0.1,

	max_number_of_rules		=> 100,
};

my @names = qw(
	class
	ruletype_longname
	ruletype_shortname
	linguistic_pattern
	order_of_arguments
	patterntype
	subject
	predicate
	object
	condAB
	condBA
	supA
	supB
	supAB
	AllConf
	Coherence
	Cosine
	IR
	Kulczynski
	MaxConf
	string
);

my $parameterstring_nonlocalized = join("-",
	$CFG->{min_entities_per_class},
	$CFG->{max_entities_per_class},
	$CFG->{min_onegram_length},
	$CFG->{min_pattern_count},
	$CFG->{orig_min_supA},
	$CFG->{orig_min_supB},
	$CFG->{orig_min_supAB}
);

my $parameterstring_localized = join("-",
	$CFG->{min_entities_per_class},
	$CFG->{max_entities_per_class},
	$CFG->{min_anchor_count},
	$CFG->{min_propertyonegram_length},
	$CFG->{min_propertypattern_count},
	$CFG->{min_propertystring_length},
	$CFG->{max_propertystring_length},
	$CFG->{orig_min_supA},
	$CFG->{orig_min_supB},
	$CFG->{orig_min_supAB}
);

my $parameterstring_counting = join("-",
        $CFG->{min_entities_per_class},
        $CFG->{max_entities_per_class},
        $CFG->{min_onegram_length},
        $CFG->{min_pattern_count},
        $CFG->{min_anchor_count},
        $CFG->{min_propertyonegram_length},
        $CFG->{min_propertypattern_count},
        $CFG->{min_propertystring_length},
        $CFG->{max_propertystring_length},
        $CFG->{orig_min_supA},
        $CFG->{orig_min_supB},
        $CFG->{orig_min_supAB},
	$CFG->{counting_min_supA},
	$CFG->{counting_min_supB},
	$CFG->{counting_min_supAB},
	$CFG->{counting_min_condBA},
	$CFG->{counting_min_condAB}
);

my $parameterstring_collecting = join("-",
        $CFG->{min_entities_per_class},
        $CFG->{max_entities_per_class},
        $CFG->{min_onegram_length},
        $CFG->{min_pattern_count},
        $CFG->{min_anchor_count},
        $CFG->{min_propertyonegram_length},
        $CFG->{min_propertypattern_count},
        $CFG->{min_propertystring_length},
        $CFG->{max_propertystring_length},
        $CFG->{orig_min_supA},
        $CFG->{orig_min_supB},
        $CFG->{orig_min_supAB},
        $CFG->{collecting_min_supA},
        $CFG->{collecting_min_supB},
        $CFG->{collecting_min_supAB},
        $CFG->{collecting_min_condBA},
        $CFG->{collecting_min_condAB},
        $CFG->{max_number_of_rules}
);


mkdir "../data-v3/examples/" if not -d "../data-v3/examples/";

my $S = {};
my $P = {};

my $cnt = 0;
foreach my $filename (sort glob("../results-v3/rules-*.bz2")){
        next if not ($filename =~ m/$parameterstring_nonlocalized/ or $filename =~ m/$parameterstring_localized/);

	$cnt++;
	#last if $cnt > 10; # TODO remove

	#print Dump $S->{count_rules_per_ruletype}; #<STDIN>; # TODO remove

	print "< ($cnt) $filename\n";
	my $zh = IO::Uncompress::Bunzip2->new(
        	$filename,
                { AutoClose => 1, Transparent => 1, }
        ) or die "IO::Uncompress::Bunzip2 failed: $Bunzip2Error\n";

	my $header = <$zh>;

	my $csv = Text::CSV->new ({ binary => 1, eol => $/ });

	my ($ruletype, $class);
	my $string = join("-", $CFG->{min_entities_per_class}, $CFG->{max_entities_per_class});
	if($filename =~ m/rules-(predict.*)-dbo-(.*)-$string/){
		($ruletype, $class) = ($1, $2);
		print " -> $class\n"; #<STDIN>;
	} else {
		print "unexpected filename $filename\n"; <STDIN>;
	}

	my @D = ();
	my $cntl=0;
        while(my $line=<$zh>){
		#last if ++$cntl > 10000; # TODO remove
        	$csv->parse ($line);
     		my @fields = $csv->fields();
		my $values = {};
		for(my $i=0; $i<scalar @names; $i++){
			$values->{$names[$i]} = $fields[$i];
		}
	
		# these cases should not appear in the data anymore
		if($values->{predicate} eq "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" and $values->{object} eq $values->{class}){
			next;
			# TODO: maybe count these cases
		}
		
		if(
			$values->{supA} >= $CFG->{collecting_min_supA} and
			$values->{supB} >= $CFG->{collecting_min_supB} and
			$values->{supAB} >= $CFG->{collecting_min_supAB} and
			$values->{condAB} >= $CFG->{collecting_min_condAB} and
			$values->{condBA} >= $CFG->{collecting_min_condBA}
		){
			push(@D, $values);
		}

		if(
                        $values->{supA} >= $CFG->{counting_min_supA} and
                        $values->{supB} >= $CFG->{counting_min_supB} and
                        $values->{supAB} >= $CFG->{counting_min_supAB} and
                        $values->{condAB} >= $CFG->{counting_min_condAB} and
                        $values->{condBA} >= $CFG->{counting_min_condBA}
                ){		
			$S->{count_rules_per_ruletype}->{$values->{ruletype_longname}}++;
			$S->{total_number_of_rules}++;

			#my $supA_bin = int($values->{supA}/10)*10 . "-" . (int(1 + $values->{supA}/10)*10-1);
                	#my $supB_bin = int($values->{supB}/10)*10 . "-" . (int(1 + $values->{supB}/10)*10-1);
			#$S->{sup_combination_histogram}->{$supA_bin . "_" . $supB_bin}++;

			foreach my $measure (qw(AllConf Coherence Cosine IR Kulczynski MaxConf)){
				my $low = sprintf("%0.1f", $values->{$measure});
				my $high = sprintf("%0.1f", $low + 0.1);
				$S->{histograms}->{interestingness}->{$measure}->{$values->{ruletype_longname}}->{$low . "-" . $high}++;
			}

                        $S->{histograms}->{supA}->{$values->{ruletype_longname}}->{$values->{supA}}++;
                        $S->{histograms}->{supB}->{$values->{ruletype_longname}}->{$values->{supB}}++;
                        $S->{histograms}->{supAB}->{$values->{ruletype_longname}}->{$values->{supAB}}++;

			my $condBA_low = sprintf("%0.01f", $values->{condBA});
                        my $condBA_high = sprintf("%0.01f", $condBA_low + 0.1);
                        $S->{histograms}->{condBA}->{$values->{ruletype_longname}}->{$condBA_low . "-" . $condBA_high}++;

			my $condAB_low = sprintf("%0.01f", $values->{condAB});
                        my $condAB_high = sprintf("%0.01f", $condAB_low + 0.1);
                        $S->{histograms}->{condAB}->{$values->{ruletype_longname}}->{$condAB_low . "-" . $condAB_high}++;
		}
	} # while line

	foreach my $measure (qw(
		AllConf
		Coherence
		Cosine
		IR
		Kulczynski
		MaxConf
	)){
		mkdir "../data-v3/examples/$parameterstring_collecting" if not -d "../data-v3/examples/$parameterstring_collecting";
		my $outfile = "../data-v3/examples/$parameterstring_collecting/" . $class . "-" . $ruletype . "-" . $measure . ".csv";
		if(scalar @D){
			open(DAT,">$outfile");
			print DAT "value,supA,supB,supAB,condBA,condAB,string\n";
			print " > $outfile\n";
        		my $cnt = 0;
                	foreach my $rule (reverse sort { $a->{$measure} <=> $b->{$measure} } @D){
                		last if $cnt >= $CFG->{max_number_of_rules};
                	        $cnt++;
                	        push(@{$S->{$class}->{$ruletype}->{$measure}}, clone($rule));

				my (
					$string,
					$supA,
					$supB,
					$supAB,
					$condBA,
					$condAB,
					$value
				) = (
					$rule->{string},
					$rule->{supA},
					$rule->{supB},
					$rule->{supAB},
					$rule->{condBA},
					$rule->{condAB},
					$rule->{$measure}
				);
				#my $latexdata = "\\newcommand*\\supA{" . $rule->{supA}. "}\\newcommand*\\supB{" . $rule->{supB} . "}\\newcommand*\\supAB{" . $rule->{supAB} . "}\\newcommand*\\condBA{" . $rule->{condBA} . "}\\newcommand*\\condAB{" . $rule->{condAB} . "}\\newcommand*\\AllConf{" . $rule->{AllConf} . "}\\newcommand*\\Coherence{" . $rule->{Coherence} . "}\\newcommand*\\Cosine{" . $rule->{Cosine} . "}\\newcommand*\\IR{" . $rule->{IR} . "}\\newcommand*\\Kulczynski{" . $rule->{Kulczynski} . "}\\newcommand*\\MaxConf{" . $rule->{MaxConf} ."}";
                                #print DAT "$value,$supA,$supB,$supAB,$string\t$latexdata\n";
				print DAT "$value,$supA,$supB,$supAB,$condBA,$condAB,$string\n";
               		}
			close DAT;
		}
	}
} # foreach filename
print "cnt $cnt\n";

open(DAT,">../data-v3/variables-$parameterstring_counting.tex");
print Dump $S->{count_rules_per_ruletype};
foreach my $ruletype (qw(
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
	my $name = $ruletype;
	$name =~ s/_//g;
	if(exists $S->{count_rules_per_ruletype}->{$ruletype}){
		print DAT "\\newcommand\\count${name}{" . $S->{count_rules_per_ruletype}->{$ruletype}. "}\n";
	} else {
		print DAT "\\newcommand\\count${name}{0}\n";
	}
}
print DAT "\\newcommand\\totalnumberofrules{" . $S->{total_number_of_rules} . "}\n";
close DAT;

DumpFile("../data-v3/statistics-$parameterstring_counting.yml", $S);
DumpFile("../data-v3/histograms-$parameterstring_counting.yml", $S->{histograms});

