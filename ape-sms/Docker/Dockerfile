FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} ape-sms9525.jar
EXPOSE 9525
ENTRYPOINT ["java", "-jar", "/ape-sms9525.jar"]