sub clean_text {
	my $string = shift;

	#print "ORIG <$string>\n";
	#$string =~ s/\x{2013}/-/g;
	#$string =~ s/\\u2013/–/g;
	#$string =~ s/\\u2014/—/g;


	#$string = unescape($string);

	#print "NEW <$string>\n";<STDIN>;


	$string =~ s/\\u([\dA-Fa-f]{4})/chr(hex($1))/eg;

	return $string;
}
1;
