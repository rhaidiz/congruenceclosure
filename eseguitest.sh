#!/bin/bash
for FILE in $(ls $1/*.txt)
do
	TXT=$(java -Xmx1g -jar lcc.jar $3 $FILE) 
	echo $TXT
	echo $TXT >> $2
done
