FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/avod-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} avod-springboot.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/avod-springboot.jar"]

LABEL authors="parkhyeongju"