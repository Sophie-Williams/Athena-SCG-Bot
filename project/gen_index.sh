#! /bin/bash

if [ "$1" == "" ]; then
    cat > /dev/stderr <<EOF
  ** usage: gen_index.sh <BASDIR> <TITLE>

      Generates an html listing titled TITLE (to Std Output) of
        the current directory, using BASEDIR as the root directory
        for naming.
EOF
    exit 1
fi


BASE=$1
TITLE=$2

CURR=`pwd | egrep -o "$BASE.*$"`

DIRS=`ls --color=never -dl * | grep "^d" | awk '{ print $8 }'`
JFILES=`ls --color=never -dl * | grep "^-" | egrep "\.java$" | awk '{ print $8 }'`
OFILES=`ls --color=never -dl * | grep "^-" | egrep -v "(*\.java$)|(\.class$)|(index\.html$)|(\.java\.html$)|(~$)" | awk '{ print $8 }'`

cat <<EOF
<html>
  <head>
     <title>$TITLE Listing: $CURR/</title>
     <style>
         table{
            border:dashed 1px blue;
            padding: 10px;
            width:500px;
         }
         td{
            padding-right: 10px;
            padding-left: 10px;
         }         
     </style>
  </head>
<body>
  <center>
     <h3>$TITLE Listing<br><h3>
     <h2><tt>$CURR/</tt></h2>

     <table>
EOF

echo "        <tr><th colspan='3'>Directories</th></tr>"
echo "           <tr><td colspan='3'><hr/></td></tr>"
if [ "$CURR" != "$BASE" ]; then
    echo "        <tr><td colspan='3'><a href='../'>../</td></tr>"
fi

for i in $DIRS; do
    echo "        <tr><td colspan='3'><a href='$i'>$i/</a></td></tr>"
done
echo "        <tr></tr>"

if [ "$JFILES" != "" ]; then
    echo "        <tr><th colspan='3'>Java Files</th></tr>"
    echo "           <tr><td colspan='3'><hr/></td></tr>"
    for i in $JFILES; do
        if [ ! -f "$i.html" ]; then
            HTMLize $i &> /dev/null
        fi
        echo "        <tr><td>$i</td><td><a href='$i'>Plain</a></td><td><a href='$i.html'>HTML</a></td></tr>"
    done
    echo "        <tr>&nbsp;</tr>"
fi


if [ "$OFILES" != "" ]; then
    echo "        <tr><th colspan='3'>Other Files</th></tr>"
    echo "           <tr><td colspan='3'><hr/></td></tr>"
    for i in $OFILES; do
        echo $i | egrep "(\.class$)|(index\.html$)|(~$)" &> /dev/null
        if [ $? == 1 ]; then
            echo $i | grep ".html$" &> /dev/null
            if [ $? == 1 ]; then
                echo "        <tr><td>$i</td><td><a href='$i'>Plain</a></td><td></td></tr>"
            else
                echo "        <tr><td>$i</td><td></td><td><a href='$i'>HTML</a></td></tr>"
            fi
        fi
    done
fi
cat <<EOF
     </table>
  </center>
</body>
</html>
EOF