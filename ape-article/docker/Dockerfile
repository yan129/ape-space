FROM openjdk:8-jdk-alpine
ARG JAR_FILE
ARG JASYPT_PASSWORD
ENV jasyptPassword=${JASYPT_PASSWORD}
COPY ${JAR_FILE} ape-article9527.jar
EXPOSE 9527
#ENTRYPOINT ["java", "-jar", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "/ape-article9527.jar"]
ENTRYPOINT ["sh", "-c", "java -jar -Djasypt.encryptor.password=$jasyptPassword /ape-article9527.jar"]