FROM maven:3.9.4-eclipse-temurin-21 AS build
LABEL authors="Daniele"

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
LABEL authors="Daniele"

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]