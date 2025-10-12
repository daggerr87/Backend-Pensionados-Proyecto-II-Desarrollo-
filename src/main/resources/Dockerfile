FROM openjdk:21-jdk
ARG JAR_FILE=./target/back-pensionados-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_pensionados_back.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_pensionados_back.jar"]