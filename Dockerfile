# Use the official OpenJDK base image with Java 17
FROM openjdk:19-jdk-slim-buster

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files (gradlew and gradlew.bat)
COPY gradlew .
COPY gradle ./gradle

# Copy the project files
COPY . .

# Run Gradle build to download dependencies and build the application
RUN ./gradlew build

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Specify the command to run your Spring Boot application
CMD ["java", "-jar", "build/libs/forum-0.0.1-SNAPSHOT.jar"]