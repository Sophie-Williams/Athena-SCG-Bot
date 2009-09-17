#! /bin/bash

cp ../SCGLibs/scglibs.jar admin.jar

jar -umf admin/admin.mf admin.jar \
    `find ./ -name '*.class'`     \
    `find ./ -name '*.java'`      \
    prereg/prereg.html            \
    `find files/ -name '*' -type f | grep -v '.svn'` \
    makeJar.sh overview.html \
    .classpath .project admin/admin.mf
