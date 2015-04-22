#!/bin/sh
rm -f tmp.dict; cat ./src/main/resources/dict/english.0 | sort -u > tmp.dict; rm -f ./src/main/resources/dict/english.1; awk ' {print;} { print ""; }' tmp.dict > ./src/main/resources/dict/english.1
