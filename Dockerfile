FROM exoplatform/jdk:openjdk-21-ubuntu-2404
ARG JAR_FILE=target/jitsi-call.jar
COPY ${JAR_FILE} /jitsi-call.jar
ENTRYPOINT ["java", "-jar", "/jitsi-call.jar"]
