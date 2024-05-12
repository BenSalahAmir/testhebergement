# Use the official OpenJDK 17 image as a parent image
FROM adoptopenjdk/openjdk17:alpine-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY target/spring-boot-mongodb-login-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port that your Spring Boot application uses
EXPOSE 9098

# Run the JAR file when the container launches
CMD ["java", "-jar", "app.jar"]
