FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/jitsi-2.3.4.RELEASE.jar
COPY ${JAR_FILE} jitsi-2.3.4.RELEASE.jar
ENTRYPOINT ["java","-jar", "/jitsi-2.3.4.RELEASE.jar"]
