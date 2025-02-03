FROM maven:3.9-amazoncorretto-17 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:17

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY .env .env  

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
