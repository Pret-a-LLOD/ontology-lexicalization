#!/usr/bin/perl -w
use Clone qw(clone);
use YAML qw(Dump DumpFile);

my @incremental_result_files = (
	#{
	#	filename => "/opt/massive_correlation/results/incremental-results-americanonly-1000-2020-02-29_14-13-06.txt",
	#	M => "M1",
	#	alpha => "JJ",
	#	beta => -1,
	#	token => "American"
	#},
	#{
	#	filename => "/opt/massive_correlation/results/incremental-results-germanonly-1000-2020-02-27_14-08-26.txt",
	#	M => "M1",
	#	alpha => "JJ",
	#	beta => "-1",
	#	token => "German",
	#},
	#{
	#	filename => "/opt/massive_correlation/results/incremental-results-greekonly-1000-2020-02-27_20-48-29.txt",
	#	M => "M1",
	#	alpha => "JJ",
	#	beta => -1,
	#	token => "Greek",
	#},
	#{
	#	filename => "/opt/massive_correlation/results/incremental-results-canadianonly-1000-2020-02-28_21-53-56.txt",
	#	M => "M1",
	#	alpha => "JJ",
	#	beta => -1,
	#	token => "Canadian"
	#},
	
	{
		filename => "/opt/massive_correlation/code/EnglishDBpediaAbstracts/lamgrapami/results/incremental-results-M1-JJ--1-Greek-1000-DI1I4M-500000-10-MIS_e_emb-strategy-5bf.txt",
		M => "M1",
		alpha => "JJ",
		beta => "-1",
		token => "Greek"
	},

        {
                filename => "/opt/massive_correlation/code/EnglishDBpediaAbstracts/lamgrapami/results/incremental-results-M1-JJ--1-Dutch-1000-DI1I4M-500000-10-MIS_e_emb-strategy-5bf.txt",
                M => "M1",
                alpha => "JJ",
                beta => "-1",
                token => "Dutch"
        },

	{
                filename => "/opt/massive_correlation/code/EnglishDBpediaAbstracts/lamgrapami/results/incremental-results-M1-JJ--1-German-1000-DI1I4M-500000-10-MIS_e_emb-strategy-5bf.txt",
                M => "M1",
                alpha => "JJ",
                beta => "-1",
                token => "German"
        },

        {
                filename => "/opt/massive_correlation/code/EnglishDBpediaAbstracts/lamgrapami/results/incremental-results-M1-JJ--1-Hungarian-American-1000-DI1I4M-500000-10-MIS_e_emb-strategy-5bf.txt",
                M => "M1",
                alpha => "JJ",
                beta => "-1",
                token => "Hungarian-American"
        },

	#{
	#        filename => "/opt/massive_correlation/code/EnglishDBpediaAbstracts/lamgrapami/results/results-M1-JJ--1-Chinese-speaking-1000-DI1I4M-500000-5-MIS_e_emb-strategy-5bf.txt",
	#        M => "M1",
	#        alpha => "JJ",
	#        beta => "-1",
	#        token => "Chinese-speaking"
	#},

	{
		filename => "/opt/massive_correlation/code/EnglishDBpediaAbstracts/lamgrapami/results/incremental-results-M1-JJ--1-Swedish-1000-DI1I4M-500000-10-MIS_e_emb-strategy-5bf.txt",
		M => "M1",
		alpha => "JJ",
		beta => "-1",
		token => "Swedish",
	},

	{
		filename => "/opt/massive_correlation/code/EnglishDBpediaAbstracts/lamgrapami/results/incremental-results-M1-JJ--1-Gothic-1000-DI1I4M-500000-10-MIS_e_emb-strategy-5bf.txt",
		M => "M1",
		alpha => "JJ",
		beta => "-1",
		token => "Gothic",
	},

);

# TODO: Added pattern P7532 to final (2):

# TODO: read in nn-incremental files

my $HEX = "[0-9]|[A-F]|[a-f]";
my $UCHAR = "'\u'$HEX$HEX$HEX$HEX|'\U'$HEX$HEX$HEX$HEX$HEX$HEX$HEX$HEX";
my $IRIREF = "<([^\x{00}-\x{20}<>\"{}|^`\]|$UCHAR)*>";

my $dictionary = {};

foreach my $file (@incremental_result_files){

	my $filename = $file->{filename};
	print "with file <$filename>\n"; #<STDIN>;

	open(DAT,"<$filename");

	my $patterns = {};


	my $in_extended_pattern_block = 0;
	my $in_initial_pattern_block = 0;
	my $in_probably_not_frequent_block = 0;
	my $extended_pattern_block_basepatternID = undef;
	my $extended_pattern_block_patternID = undef;
	my $extended_pattern_triples = [];
	my $extended_pattern_block_setID = undef;

	while(defined(my $line = <DAT>)){
		next if $line =~ m/\A#/;
		next if $line =~ m/\AWrit+en output to/;

		$line =~ s/\n\Z//;

		#print "<$line>\n";

		if($line =~ m/\ACreated initial pattern/){
			$in_initial_pattern_block = 1; #print " -> in_initial_pattern_block = 1\n"; <STDIN>;
		}

		elsif($line =~ m/\APattern is probably not frequent/){
			$in_probably_not_frequent_block = 1;
		}

		elsif($line =~ m/Extended pattern (P\d+) with pattern P(\d+), resulting in pattern (P\d+):/){
			$extended_pattern_block_basepatternID = $1;
			$extended_pattern_block_patternID = $3;
			$in_extended_pattern_block = 1;
			if(exists $patterns->{$extended_pattern_block_basepatternID}){
				#print "Removed base pattern $extended_pattern_block_basepatternID\n";
				delete $patterns->{$extended_pattern_block_basepatternID};
			}
		}
		elsif($in_extended_pattern_block){
			if($line =~ m/\A\s+L (.+)\Z/){
				my $triple = $1;
				if($triple =~ m/\A?v0 <http:\/\/example.org\/memberOfSet> <http:\/\/example.org\/(.+) <.*> \./){
					$extended_pattern_block_setID = $1;
				} else {
					my $obj = &parse_NT_into_obj($1); #print Dump $obj; <STDIN>;
					push(@{$extended_pattern_triples}, $obj);
				}
			} elsif($line =~ m/\A\s+S (.+)\Z/){
				# do nothing
			} else {
				$in_extended_pattern_block = 0;
				$patterns->{$extended_pattern_block_patternID} = {
					gp => clone($extended_pattern_triples),
					basePattern => $extended_pattern_block_basepatternID,
					setID => $extended_pattern_block_setID,
				};
				$extended_pattern_block_basepatternID = undef;
				$extended_pattern_block_patternID = undef;
				$extended_pattern_triples = [];
				$extended_pattern_block_setID = 0;
			}
		}
		elsif($in_initial_pattern_block and $line =~ m/\A.+\Z/){
			# ignore line
			#print " -> ignore\n";
		}
		elsif($in_initial_pattern_block and not $line) {
			$in_initial_pattern_block = 0;
			#print " -> in_initial_pattern_block = 0\n";
		}
		elsif($in_probably_not_frequent_block and $line =~ m/\A.+\Z/){
                        # ignore line
                        #print " -> ignore\n";
                }
                elsif($in_probably_not_frequent_block and not $line) {
                        $in_probably_not_frequent_block = 0;
                        #print " -> in_initial_pattern_block = 0\n";
                }

		else {
			print "unexpected line $line\n";
		}
	}	 
	close DAT;

	my $array = [];

	foreach my $patternID (keys %{$patterns}){
		# ignore patterns that only consist of ?P? triple patterns,otherwise, the pattern is not interesting
	
		#my $interesting = 0;
		#foreach my $tp (@{$patterns->{$patternID}->{gp}}){
		#	next if $tp->{p}->{type} eq "uri" and $tp->{p}->{uri} eq "<http://www.example.org/relatedToPattern>";
		#	if($tp->{s}->{type} eq "variable" and $tp->{p}->{type} eq "uri" and $tp->{o}->{type} eq "variable"){
		#		# not interesting
		#	} else {
		#		$interesting = 1;
		#	}
		#}
		#next if not $interesting;

		#print Dump $patterns->{$patternID}; <STDIN>;
		#if(scalar @{$patterns->{$patternID}->{gp}} >= 4){
		#	print Dump { pattern => $patterns->{$patternID}->{gp}, alpha => $file->{alpha}, beta => $file->{beta}, token => $file->{token} }; <STDIN>;
		#}
		push(@{$array}, { gp => $patterns->{$patternID}->{gp}  });	
	}

	$dictionary->{$file->{M}}->{$file->{alpha}}->{$file->{beta}}->{$file->{token}} = $array;
}

#print Dump { dictionary => $dictionary };

DumpFile("/opt/massive_correlation/results/new_dictionary.yml", $dictionary);

sub parse_NT_into_obj {
	my $string = shift;
	
	if($string =~ m/($IRIREF) ($IRIREF) \"(.*)\"(.*)/){
		my ($S, $P, $O, $X) = ($1, $3, $5, $6);
		# datatyped
		if($X =~ m/^\^\^<(.*)>\s\./){
			return {
				s => { type => "uri", uri => $S },
				p => { type => "uri", uri => $P },
				o => { type => "datatyped-literal", datatype => $1, value => $O }
			};
		}
		# language-tagged
		elsif($X =~ m/^\@(.*)\s\./){
			return {
                                s => { type => "uri", uri => $S },
                                p => { type => "uri", uri => $P },
                                o => { type => "languagetagged-literal", language => $1, value => $O }
                        };
		}
		# plain
		else {
			return {
                                s => { type => "uri", uri => $S },
                                p => { type => "uri", uri => $P },
                                o => { type => "plain-literal", value => $O }
                        };
		}
	}

	elsif($string =~ m/($IRIREF) (\?v\d+) \"(.*)\"(.*)/){ print "I"; <STDIN> }
	
	elsif($string =~ m/(\?v\d+) ($IRIREF) \"(.*)\"(.*)/){
                my ($S, $P, $O, $X) = ($1, $2, $4, $5);

		# datatyped
                if($X =~ m/^\^\^<(.*)>\s\./){
                        return {
                                s => { type => "variable", name => $S },
                                p => { type => "uri", uri => $P },
                                o => { type => "datatyped-literal", datatype => $1, value => $O }
                        };
                }
                # language-tagged
                elsif($X =~ m/^\@(.*)\s\./){
                        return {
                                s => { type => "variable", name => $S }, 
                                p => { type => "uri", uri => $P }, 
                                o => { type => "languagetagged-literal", language => $1, value => $O }
                        };
                }
                # plain
                else {
                        return {
                                s => { type => "variable", name => $S }, 
                                p => { type => "uri", uri => $P }, 
                                o => { type => "plain-literal", value => $O }
                        };
                }
	}

        elsif($string =~ m/(\?v\d+) (\?v\d+) \"(.*)\"(.*)/){
		my ($S, $P, $O, $X) = ($1, $2, $3, $4);
		# datatyped
                if($X =~ m/^\^\^<(.*)>\s\./){
                        return {
                                s => { type => "variable", name => $S },
                                p => { type => "variable", name => $P },
                                o => { type => "datatyped-literal", datatype => $1, value => $O }
                        };
                }
                # language-tagged
                elsif($X =~ m/^\@(.*)\s\./){
                        return {
                                s => { type => "variable", name => $S },
                                p => { type => "variable", name => $P },
                                o => { type => "languagetagged-literal", language => $1, value => $O }
                        };
                }
                # plain
                else {
                        return {
                                s => { type => "variable", name => $S },
                                p => { type => "variable", name => $P },
                                o => { type => "plain-literal", value => $O }
                        };
                }
	}

	elsif($string =~ m/($IRIREF) ($IRIREF) (\?v\d+)/){
		my ($S, $P, $O, $X) = ($1, $3, $5);
                return { # fine
                        s => { type => "uri", uri => $S },
                        p => { type => "uri", uri => $P },
	                o => { type => "variable", name => $O },
                };
	}

        elsif($string =~ m/($IRIREF) (\?v\d+) ($IRIREF)/){ print "X"; <STDIN>; }

	elsif($string =~ m/(\?v\d+) ($IRIREF) ($IRIREF)/){
		my ($S, $P, $O, $X) = ($1, $2, $4);
		return { # fine
			s => { type => "variable", name => $S },
			p => { type => "uri", uri => $P },
			o => { type => "uri", uri => $O }
		};
	}

	elsif($string =~ m/(\?v\d+) (\?v\d+) ($IRIREF)/){
		my ($S, $P, $O, $X) = ($1, $2, $3);
		return { # fine
			s => { type => "variable", name => $S },
			p => { type => "variable", name => $P },
			o => { type => "uri", uri => $O }
		};
	}
        
	elsif($string =~ m/(\?v\d+) ($IRIREF) (\?v\d+)/){
		my ($S, $P, $O) = ($1, $2, $4);
                return { # fine
                        s => { type => "variable", name => $S }, 
                        p => { type => "uri", uri => $P },
                        o => { type => "variable", name => $O }
                };
	}

	elsif($string =~ m/($IRIREF) (\?v\d+) (\?v\d+)/){
		my ($S, $P, $O) = ($1, $3, $4);
                return { # fine
                        s => { type => "uri", uri => $S },
                        p => { type => "variable", name => $P },
                        o => { type => "variable", name => $O }
                };
	}

        elsif($string =~ m/($IRIREF) ($IRIREF) (\?v\d+)/){ print "D"; <STDIN> }
        
	elsif($string =~ m/(\?v\d+) (\?v\d+) (\?v\d+)/){ print "E"; <STDIN> }

	else { print "format unexpected:\n$string\n"; <STDIN>; }
	
	# TODO: also bnodes
}
