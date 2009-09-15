#! /bin/bash

pushd ../

tar -czf Player/player.tgz \
    `find Player -name '*.java'`  \
    Player/{makeJar.sh,overview.html} \
    Player/files/* \
    Player/{.classpath,.project} \
    Player/player/player.mf

popd