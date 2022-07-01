FROM openjdk:8-jdk-alpine
ARG JAR_FILE
ARG JASYPT_PASSWORD
ENV jasyptPassword=${JASYPT_PASSWORD}
COPY ${JAR_FILE} ape-oss9536.jar
EXPOSE 9536
#ENTRYPOINT ["java", "-jar", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "/ape-oss9536.jar"]
ENTRYPOINT ["sh", "-c", "java -jar -Djasypt.encryptor.password=$jasyptPassword /ape-oss9536.jar"]
