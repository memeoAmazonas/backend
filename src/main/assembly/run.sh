RELEASE=${project.artifactId}-${project.version}.jar 
export RELEASE

java -Xmx756M -jar $RELEASE > output.log 2>&1 &
