# File: Dockerfile
# This Dockerfile uses a multi-stage build for a more secure and smaller final image.

# --- Build Stage ---
# Use an official Maven image to build the application .jar file.
# It has Java and Maven pre-installed.
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
```yaml
# File: docker-compose.yml
# This file orchestrates the running of our application and database containers.

version: '3.8'

services:
  # The PostgreSQL Database Service
  postgres-db:
    image: postgres:14.1
    container_name: telemedicine-postgres
    environment:
      # These environment variables are used by the postgres image to create the database and user
      POSTGRES_DB: telemedicine_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      # Maps the container's port 5432 to the host machine's port 5432
      - "5432:5432"
    volumes:
      # This persists the database data on the host machine, so it's not lost when the container stops
      - postgres-data:/var/lib/postgresql/data

  # The Spring Boot Application Service
  telemedicine-app:
    # Build the image from the Dockerfile in the current directory
    build: .
    container_name: telemedicine-app
    depends_on:
      # This ensures the database container starts before the application container
      - postgres-db
    ports:
      # Maps the container's port 8080 to the host machine's port 8080
      - "8080:8080"
    environment:
      # These environment variables override the ones in application.properties
      # We use the service name 'postgres-db' as the host, Docker's internal DNS will resolve it
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/telemedicine_db
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=Vishal@12#34
      # This tells Hibernate to wait for the database to be ready
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Defines the named volume used by the postgres-db service
volumes:
  postgres-data: