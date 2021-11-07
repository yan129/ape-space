FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} ape-eureka7002.jar
EXPOSE 7002
ENTRYPOINT ["java", "-jar", "/ape-eureka7002.jar"]