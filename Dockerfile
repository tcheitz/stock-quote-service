# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app
ARG JAR_FILE=target/*.jar
# Copy the jar file into the container
COPY ${JAR_FILE} /app/stock-quotes-service.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "stock-quotes-service.jar"]
