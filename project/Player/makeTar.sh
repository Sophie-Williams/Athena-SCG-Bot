#! /bin/bash

# Build a Tar/GZ archive that includes everything to get started

dir=`pwd | egrep -o '[^/]+$'`
pushd ../
tar -czf $dir/player.tgz \
    `find $dir -name '*.java'`  \
    `find $dir/files/ -name '*' -type f | grep -v '.svn'` \
    $dir/{makeTar.sh,makeSubmission.sh,overview.html} \
    $dir/{.classpath,.project,scglibs.jar,teamreadme.txt} \
    $dir/player/player.mf
popd