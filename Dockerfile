FROM amazoncorretto:21.0.4-alpine
LABEL maintainer="p.marko09@yahoo.com"
COPY target/github-proxy2-0.0.1-SNAPSHOT.jar /app/github-proxy2.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/github-proxy2.jar"]