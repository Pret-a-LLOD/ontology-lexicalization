#!/usr/bin/perl -w
use strict;
use YAML::Syck qw(LoadFile);
use URL::Encode qw(url_encode_utf8);


# purpose of this script? I switched off the compression of entity data files in experiment.pl because this slowed down the processing.
# this script compressed all entity files that are not compressed yet.

my $folder_length = 4;
my $CFG = {
        min_anchor_count                => 10,
};

print "loading ...\n";
my $data = LoadFile("../data-v3/entity_to_frequent_classes-100-10000.yml");
print "done.\n";

foreach my $e (keys %{$data}){
	
my $e_enc;
        if($e =~ m/http:\/\/dbpedia.org\/resource\/(.*)/){
                $e_enc = "dbr-" . url_encode_utf8($1);
        } else {
                print "unexpected entity URI namespace <$e>\n";
                next;
        }
        my $last = substr($e_enc, -$folder_length);
	foreach my $pos (qw(sub obj)){
    		my $entitydatafilename = "../data-v3/data_per_entity/$last/$e_enc-$pos-" . $CFG->{min_anchor_count} . ".ttl";

		if(-s $entitydatafilename and not -s "$entitydatafilename.bz2"){
			system("bzip2 $entitydatafilename");
			print "compressing $entitydatafilename\n"; #<STDIN>;
			unlink $entitydatafilename;
		}
		elsif(-s $entitydatafilename and -s "$entitydatafilename.bz2"){
                        unlink $entitydatafilename;
		}
	}
}
