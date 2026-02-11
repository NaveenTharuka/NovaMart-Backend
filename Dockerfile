# Use Maven + Java preinstalled
<<<<<<< HEAD
FROM maven:3.9.9-eclipse-temurin-21 AS build
=======
FROM maven:3.9.4-eclipse-temurin-21 AS build
>>>>>>> 787b0b8753dfd9d21546596d0053a52e02021399

WORKDIR /app
COPY . .

# Build the Spring Boot project (skip tests for speed)
RUN mvn clean package -DskipTests

# Use lightweight JDK runtime for running the app
<<<<<<< HEAD
FROM eclipse-temurin:21-jdk
=======
FROM eclipse-temurin:17-jdk
>>>>>>> 787b0b8753dfd9d21546596d0053a52e02021399
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Use Render's dynamic port
ENV PORT 8080
CMD ["java", "-jar", "app.jar"]
