FROM openjdk:8-jdk-alpine
ARG JAR_FILE
ENV JASYPT_PASSWORD ${JASYPT_PASSWORD}
COPY ${JAR_FILE} ape-user9526.jar
EXPOSE 9526
ENTRYPOINT ["java", "-jar", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "/ape-user9526.jar"]