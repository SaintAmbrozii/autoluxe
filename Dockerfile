FROM eclipse-temurin:17.0.10_7-jre as builder

ARG ARG_VERSION
ARG APP_NAME=autoluxe

ARG JAR_FILE=target/auto-luxe-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]



