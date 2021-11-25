FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} ape-oss9536.jar
EXPOSE 9536
ENTRYPOINT ["java", "-jar", "/ape-oss9536.jar"]
