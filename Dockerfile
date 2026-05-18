FROM eclipse-temurin:25-jdk-jammy AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package

FROM eclipse-temurin:25-jre-alpine AS production
WORKDIR /app
COPY --from=build /app/target/app-microservice.jar app-microservice.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app-microservice.jar"]
