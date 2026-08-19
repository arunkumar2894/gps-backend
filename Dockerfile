# Multi-stage Dockerfile for a Maven-built Spring Boot app
# Build stage: use Maven to compile and package
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom first to leverage Docker cache for dependencies
COPY pom.xml ./
RUN mvn -B -f pom.xml dependency:go-offline

# Copy source and package
COPY src ./src
RUN mvn -B -DskipTests package

# Run stage: use a lightweight JRE
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the packaged jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Include the static HTML assets served by the app
COPY static ./static

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
