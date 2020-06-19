#!/usr/bin/perl -w
use strict;
use YAML qw(Dump);
use Storable;

my $D = retrieve($ARGV[0]);
print Dump $D;

