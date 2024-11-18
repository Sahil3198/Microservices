FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} authenticationservice.jar

ENTRYPOINT ["java", "-jar", "/authenticationservice.jar"]

EXPOSE 8082