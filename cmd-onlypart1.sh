#0..109
for i in {0..109}; do echo $i; perl convert-part1.pl $i > logs/convert-part1-$i.txt 2> logs/convert-part1-$i.err; done

