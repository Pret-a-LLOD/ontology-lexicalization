#0..109
#for i in {0..10}; do echo $i; perl convert-part1.pl $i > logs/convert-part1-$i.txt 2> logs/convert-part1-$i.err; perl convert-part2.pl $i > logs/convert-part2-$i.txt 2> logs/convert-part2-$i.err; perl convert-part3.pl $i > logs/convert-part3-$i.txt 2> logs/convert-part3-$i.err; done

for i in {1..15}; do echo $i; perl convert-part1.pl $i > logs/convert-part1-$i.txt 2> logs/convert-part1-$i.err & done

#for i in {1..10}; do echo $i; perl convert-part2.pl $i > logs/convert-part2-$i.txt 2> logs/convert-part2-$i.err & done

