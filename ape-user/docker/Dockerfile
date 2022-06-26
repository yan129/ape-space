FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} ape-user9526.jar
ENV JASYPT_PASSWORD = ""
EXPOSE 9526
ENTRYPOINT ["java", "-jar", "-Djasypt.encryptor.password=$JASYPT_PASSWORD", "/ape-user9526.jar"]