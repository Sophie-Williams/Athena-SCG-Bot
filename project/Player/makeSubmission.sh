#! /bin/bash
# *** Make the subission JARs

if [ "$1" == "" ]; then
    cat <<EOF
 ** usage: ./makeSubmission.sh <TeamName>
    - Make the submission Jars for your player.
    - Given your team name, say "MyTeam", this script will create two Jars:

       MyTeam.jar : A runnable Jar with your player's class files. The
                    course staff should be able to run your Player with
                    a command like: java -jar MyTeam.jar

       MyTeam_source.jar : A source Jar with your Java files, and your
                           teamreadme.txt file with your information
                           for the current project.
EOF
    exit 1;
fi

# Use the TeamName to name the Jars
runnable="${1}.jar";
source="${1}_source.jar";

# First the Runnable JAR without source. Just start with the
#   'scglibs.jar' and collect all the of our .class files
cp scglibs.jar $runnable
jar -umf player/player.mf $runnable \
    `find ./ -name '*.class'` &> /dev/null

# Now the source JAR.

# Grab tha Name of the current Directory
dir=`pwd | egrep -o '[^/]+$'`

# Go one level up so there's a Main directory for all the files
pushd ../
# ** Be sure to add any other files you deem necessary to the list
#      below.  If you require multiple lines follow all but the last
#      line with a backslash ('/')
jar -cf $dir/$source \
    `find ./$dir -name '*.java'` \
    $dir/teamreadme.txt

# Return to the current directory
popd