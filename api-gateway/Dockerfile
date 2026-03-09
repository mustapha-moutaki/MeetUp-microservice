FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY config-service/pom.xml config-service/pom.xml
COPY discovery-service/pom.xml discovery-service/pom.xml
COPY keynote-service/pom.xml keynote-service/pom.xml
COPY conference-service/pom.xml conference-service/pom.xml
COPY notification-service/pom.xml notification-service/pom.xml

RUN mvn -q -DskipTests -pl api-gateway -am dependency:go-offline
COPY . .
RUN mvn -q -DskipTests -pl api-gateway -am package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/api-gateway/target/api-gateway-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
