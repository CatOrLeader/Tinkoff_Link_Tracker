#
# PACKAGE STAGE
#
FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY bot/target/bot.jar bot.jar
ENTRYPOINT ["java", "-jar", "bot.jar", "--spring.profiles.active=dev"]
