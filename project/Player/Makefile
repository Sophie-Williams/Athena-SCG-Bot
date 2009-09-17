class:
		javac -classpath .:/home/chadwick/www/demeterf/demeterf.jar:../SCGLibs/ \
				*.java player/*.java || true

jar: class
		@cp ../SCGLibs/demeterfRT.jar ./player.jar
		@jar -umf player/player.mf player.jar \
				`find ./ -name '*.class'` \
				`find ./ -name '*.java'`  \
				makeJar.sh overview.html files/ \
				.classpath .project player/player.mf
		echo Adding scg
		cd ../SCGLibs && jar uf ../Player/player.jar `find . -name '*.class'`


tar:
		cd ..
		tar -czf Player/player.tgz \
				`find Player -name '*.java'`  \
				Player/{makeJar.sh,overview.html} \
				Player/files/* \
				Player/{.classpath,.project} \
				Player/player/player.mf
		cd Player


all: jar tar
