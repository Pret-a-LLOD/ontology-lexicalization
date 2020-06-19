#!/usr/bin/perl -w
use strict;

my $CFG = {
	files => {
		#"article-categories_en.nt" 			=> "A",
		#"category-labels_en.nt" 			=> "C",
		"dbpedia_2015-04.nt" 				=> "D",
		"infobox-properties_en.nt" 			=> "I1", 
		#"infobox-property-definitions_en.nt" 		=> "I2",
		#"instance-types-transitive_en.nt" 		=> "I3",
		"instance-types_en.nt" 				=> "I4",
		#"instance_types_sdtyped-dbo_en.nt" 		=> "I5",
		#"labels_en.nt" 					=> "L",
		"mappingbased-properties_en.nt" 		=> "M",
		#"skos-categories_en.nt" 			=> "S1",
		#"specific-mappingbased-properties_en.nt" 	=> "S2",
	},
};

my $string = join(q{}, sort values %{$CFG->{files}});

print " << File $string.nt >>\n";
if(not -e "/opt/massive_correlation/data/dbpedia-2015-04/$string.nt"){
	foreach my $file (keys %{$CFG->{files}}){
		print "cat $file >> $string.nt\n"; 
		system("cat /opt/massive_correlation/data/dbpedia-2015-04/$file >> /opt/massive_correlation/data/dbpedia-2015-04/$string.nt");
	}
}
if(not -e "/opt/massive_correlation/data/dbpedia-2015-04/${string}_sorted_unique.nt"){
	print "create ${string}_sorted_unique.nt ...\n";
	system("cat /opt/massive_correlation/data/dbpedia-2015-04/${string}.nt > /opt/massive_correlation/data/dbpedia-2015-04/${string}_sorted_unique.nt");
}

