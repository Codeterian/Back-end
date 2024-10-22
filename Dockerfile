FROM openjdk:17-jdk-slim

ARG FILE_DIRECTORY
# 시간대 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY $FILE_DIRECTORY/build/libs/*SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]