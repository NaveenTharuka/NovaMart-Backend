# Use Maven + Java preinstalled
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .

# Build the Spring Boot project (skip tests for speed)
RUN mvn clean package -DskipTests

# Use lightweight JDK runtime for running the app
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Use Render's dynamic port
ENV PORT 8080
CMD ["java", "-jar", "app.jar"]
