FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to download dependencies first, leveraging Docker's layer caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application, skipping the tests as they've already been run
RUN mvn clean package -DskipTests

# --- Run Stage ---
# Use a slim OpenJDK image which is much smaller than the Maven one.
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the .jar file from the 'build' stage into the current stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the Spring Boot application runs on
EXPOSE 8080

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]