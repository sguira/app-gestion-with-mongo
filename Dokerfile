FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . /app/

RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app_e_feray.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","app_e_feray.jar"]