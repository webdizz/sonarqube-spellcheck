#!/bin/sh
rm -f tmp.dict; cat ./dict/english.0 | sort -u > tmp.dict; rm -f dict/english.1; awk ' {print;} { print ""; }' tmp.dict > dict/english.1 
