FROM openjdk:17-jdk-slim

ARG FILE_DIRECTORY

RUN mkdir /app

ARG JAR_FILE=${FILE_DIRECTORY}/build/libs/*.jar

COPY ${JAR_FILE} app/app.jar

CMD ["java", "-jar", "app/app.jar", "--spring.profiles.active=prod"]