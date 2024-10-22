FROM openjdk:17-jdk-slim

ARG FILE_DIRECTORY

COPY $FILE_DIRECTORY/build/libs/*SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]