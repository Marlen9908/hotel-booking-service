FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

COPY target/hotel-booking-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
