#! /bin/bash

cp ../SCGLibs/demeterfRT.jar ./admin.jar

jar -umf admin/admin.mf admin.jar \
    `find ./ -name '*.class'`     \
    `find ./ -name '*.java'`      \
    prereg/prereg.html            \
    `find files/ -name '*' -type f | grep -v '.svn'` \
    makeJar.sh .classpath .project admin/admin.mf

pushd ../SCGLibs
jar -uf ../Admin/admin.jar `find ./ -name *.class`
popd