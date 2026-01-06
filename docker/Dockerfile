FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built jar from the host into the image
COPY target/*.jar app.jar

# Default port inside container (we'll map host ports in compose)
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
