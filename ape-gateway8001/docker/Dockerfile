FROM openjdk:8-jdk-alpine
ARG JAR_FILE
ARG JASYPT_PASSWORD
ENV jasyptPassword=${JASYPT_PASSWORD}
COPY ${JAR_FILE} ape-gateway8001.jar
EXPOSE 8001
#ENTRYPOINT ["java", "-jar", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "/ape-gateway8001.jar"]
ENTRYPOINT ["sh", "-c", "java -jar -Djasypt.encryptor.password=$jasyptPassword /ape-gateway8001.jar"]