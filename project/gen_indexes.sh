#! /bin/bash

BASE="source"
TITLE="CS4500 Source Code"

function recur {

    echo "enter $1  $2"
    pushd $1 &> /dev/null
    
    DIRS=`ls --color=never -dl * | grep "^d" | awk '{ print $8 }'`
    for i in $DIRS; do
        recur $i "$2../"
    done
    echo "leave $1  $2"
    ${2}gen_index.sh $BASE "$TITLE" > index.html
    popd &> /dev/null
}
    
recur ./ "./"