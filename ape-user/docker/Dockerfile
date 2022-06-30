FROM openjdk:8-jdk-alpine
ARG JAR_FILE
ARG JASYPT_PASSWORD
ENV jasyptPassword=${JASYPT_PASSWORD}
COPY ${JAR_FILE} ape-user9526.jar
RUN echo "${JASYPT_PASSWORD} ====="
EXPOSE 9526
ENTRYPOINT ["/bin/sh", "-c", "exce", "java", "-jar", "-Djasypt.encryptor.password=$jasyptPassword", "/ape-user9526.jar"]