#!/bin/sh


# Download The file from the given url
wget "https://www.gutenberg.org/files/2264/2264.txt"

# Prints all of the different words along with their word count
cat "2264.txt" |
    tr '[:space:]' '[\n*]' |
    tr ',' '[\n*]'  |
    tr '.' '[\n*]'  |
    tr '[:upper:]' '[:lower:]'|
    grep -v "^\s*$" |
    sort |
    uniq -c |
    sort -bn

# saves the top 20 occuring words into a file
cat "2264.txt" | 
    tr '[:space:]' '[\n*]' |
    tr ',' '[\n*]' |
    tr '.' '[\n*]'  |
    tr '[:upper:]' '[:lower:]'|
    grep -v "^\s*$" |
    sort |
    uniq -c  |
    sort -bn |
    tail -20> a.txt

#calculates the total sum of most occuring words to calculate percentage
Sum=`egrep -o "[0-9][0-9]*" "a.txt" |
     awk  '{ SUM += $1} END { print SUM }'`


printf "\n\nPercentage Graph of the most occuring 20 words \n"
#create the percentage graph
cat a.txt | awk -v s=$Sum '{ ORS=""};
    {a=$1/s;print $2;
    print "[";
    for(i=1;i<=a*200;i++) 
            print "="
    print "]",a*100,"%","\n"}'

#removes the created files
rm 2264.txt a.txt