FROM openjdk:8-jdk-alpine
ARG JAR_FILE
ARG JASYPT_PASSWORD
ENV jasyptPassword=${JASYPT_PASSWORD}
COPY ${JAR_FILE} ape-sms9525.jar
EXPOSE 9525
#ENTRYPOINT ["java", "-jar", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "/ape-sms9525.jar"]
ENTRYPOINT ["sh", "-c", "java -jar -Djasypt.encryptor.password=$jasyptPassword /ape-sms9525.jar"]