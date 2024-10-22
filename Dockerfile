# Dockerfile
FROM gradle:8.10.1-jdk17 AS build

WORKDIR /app

ARG FILE_DIRECTORY

COPY $FILE_DIRECTORY /app

RUN gradle clean bootJar

FROM openjdk:17-jdk-slim

# 환경 변수로 프로파일 설정
ENV SPRING_PROFILES_ACTIVE=prod

COPY --from=build /app/build/libs/*SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]