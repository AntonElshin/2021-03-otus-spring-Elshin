FROM openjdk:11-jre-slim
COPY /target/car-loan-dict.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
