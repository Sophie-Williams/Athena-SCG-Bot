#! /bin/bash

rm -f player.jar
cp demeterfRT.jar ./scglibs.jar

jar -uf scglibs.jar \
    `find ./ -name '*.class'` \
    `find ./ -name '*.java' | grep -v hidden` \
    makeJar.sh overview.html  \
    .classpath .project \
    demeterf.jar demeterfRT.jar
