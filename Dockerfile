FROM maven:3.8.3-jdk-11 AS build
COPY src/main/java/com/mercapp/backendspring/project /app
WORKDIR /app
RUN mvn package -DskipTests

FROM openjdk:11-jre-slim
COPY --from=build /app/target/my-application.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]