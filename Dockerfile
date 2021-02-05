FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/jitsi-1.0.x-jitsi-SNAPSHOT.jar
COPY ${JAR_FILE} jitsi-1.0.x-jitsi-SNAPSHOT.jar
ENTRYPOINT ["java","-jar", "/jitsi-1.0.x-jitsi-SNAPSHOT.jar"]
