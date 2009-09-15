#! /bin/bash

cp ../SCGLibs/demeterfRT.jar ./player.jar

jar -umf player/player.mf player.jar \
    `find ./ -name '*.class'` \
    `find ./ -name '*.java'`  \
    makeJar.sh overview.html files/ \
    .classpath .project player/player.mf

pushd ../SCGLibs
jar -uf ../Player/player.jar `find ./ -name '*.class'`
popd
