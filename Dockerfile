FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY library-service-api ./library-service-api
COPY openlibrary-api-sdk ./openlibrary-api-sdk
RUN mvn clean install

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/library-service-api/target/libraryserviceapi-*.jar /app/libraryserviceapi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/libraryserviceapi.jar"]
