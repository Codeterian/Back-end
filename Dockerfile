# Dockerfile
FROM gradle:8.10.1-jdk17 AS build

WORKDIR /app

# common 디렉토리 복사
COPY common /app/common

# 각 서비스 디렉토리 복사 (예: eureka 서비스)
COPY eureka /app/eureka

# 필요에 따라 다른 서비스도 복사
COPY user /app/user
COPY gateway /app/gateway
COPY performance /app/performance
COPY order /app/order
COPY payment /app/payment
COPY ticket /app/ticket

# Gradle 빌드 실행 (각 서비스 디렉토리에 있는 build.gradle이 사용됨)
RUN gradle clean bootJar --project-dir /app/eureka  # eureka 서비스 빌드 예시

FROM openjdk:17-jdk-slim

COPY --from=build /app/eureka/build/libs/*SNAPSHOT.jar /app.jar  # eureka 서비스의 JAR 파일 복사

CMD ["java", "-jar", "app.jar"]
