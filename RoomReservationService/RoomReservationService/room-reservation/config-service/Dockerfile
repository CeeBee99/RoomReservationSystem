FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app/config-service
COPY config-service/pom.xml .
COPY config-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/config-service/target/*.jar app.jar
COPY config-repo /config-repo
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
