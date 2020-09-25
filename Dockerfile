FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/jitsi-call-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} jitsi-call-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar", "/jitsi-call-0.0.1-SNAPSHOT.jar"]
