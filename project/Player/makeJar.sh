#! /bin/bash

javac -classpath .:/home/chadwick/www/demeterf/demeterf.jar:../SCGLibs/ \
    *.java player/*.java
cp ../SCGLibs/demeterfRT.jar ./player.jar

jar -umf player/player.mf player.jar \
    `find ./ -name '*.class'` \
    `find ./ -name '*.java'`  \
    makeJar.sh overview.html files/ \
    .classpath .project player/player.mf

pushd ../SCGLibs
jar -uf ../Player/player.jar `find ./ -name '*.class'`
popd
