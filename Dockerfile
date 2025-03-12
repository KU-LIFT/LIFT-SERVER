# OpenJDK 21 기반 Spring Boot 컨테이너
FROM openjdk:21-jdk-slim

# 프로젝트 JAR 파일 복사
ARG JAR_FILE=./build/libs/lift-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 컨테이너 실행 시 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
