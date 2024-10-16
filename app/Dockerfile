FROM bellsoft/liberica-runtime-container:jre-21-musl
EXPOSE 8083
ADD ./target/onboarding-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]
