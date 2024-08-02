FROM maven:3.9.8-amazoncorretto-17-al2023 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17-jdk-alpine
COPY --from=build /home/app/target/*.jar /usr/local/lib/stock-quotes-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/stock-quotes-service.jar"]
