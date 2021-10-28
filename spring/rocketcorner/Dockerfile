FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
CMD java -Dserver.port=$PORT $JAVA_OPTS -jar app.jar
