# Use official OpenJDK 21 slim image
FROM openjdk:21-jdk-slim AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Pre-download dependencies
RUN ./mvnw dependency:go-offline

# Copy the full project
COPY src ./src

# Package the application
RUN ./mvnw clean package -DskipTests

# ------------------- Final Image -------------------
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "app.jar"]
