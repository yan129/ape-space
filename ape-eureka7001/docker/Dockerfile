FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} ape-eureka7001.jar
EXPOSE 7001
ENTRYPOINT ["java", "-jar", "/ape-eureka7001.jar"]