# Stage 1: Build the application using Maven
FROM maven:3.9.8-eclipse-temurin-21  AS builder

# Set working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY . .

# Build the application (this will download dependencies and compile)
RUN mvn clean package

# Stage 2: Create a lightweight runtime image
#FROM openjdk:21-jdk-slim
FROM eclipse-temurin:21-jre-jammy

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar backend.jar

# Expose the port your app runs on (change if needed)
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=deployment

# Command to run the application
ENTRYPOINT ["java", "-jar", "backend.jar", "--spring.profiles.active=deployment"]
