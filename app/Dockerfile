# ./app/Dockerfile
FROM openjdk:17-alpine

# 👉 루트 경로에 있는 .jar 파일을 Docker 이미지에 복사
COPY app-*.jar app.jar

# 👉 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
