#! /bin/bash

for i in Admin Player SCGLibs; do
    pushd $i
    svn update
    popd
done

rm `find SCGLibs -name '*.java' | grep hidden`
cp index.html index.html.bak
find */ -name '*.java.html' | grep -v overview | grep -v '.svn' | xargs rm
./gen_indexes.sh
cp index.html.bak index.html