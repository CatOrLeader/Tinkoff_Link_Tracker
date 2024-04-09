#
# PACKAGE STAGE
#
FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY scrapper/target/scrapper.jar scrapper.jar
ENTRYPOINT ["java", "-jar", "scrapper.jar", "--spring.profiles.active=dev"]
