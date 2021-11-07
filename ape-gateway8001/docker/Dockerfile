FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} ape-gateway8001.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "/ape-gateway8001.jar"]