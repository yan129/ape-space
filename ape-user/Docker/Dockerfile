FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} ape-user9526.jar
EXPOSE 9526
ENTRYPOINT ["java", "-jar", "/ape-user9526.jar"]