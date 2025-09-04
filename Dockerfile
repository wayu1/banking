FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/bank-transaction-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/app.jar"]