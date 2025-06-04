FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY . .
WORKDIR /app/app
RUN gradle bootJar --no-daemon

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"] 