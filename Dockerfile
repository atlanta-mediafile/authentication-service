#
# Build stage
#
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN --mount=type=cache,target=/root/.m2 mvn -X -f /home/app/pom.xml clean package


#
# Run stage
#
FROM openjdk:23-ea-14-jdk-oraclelinux9
ARG TARGET_DIR=/home/app/target
COPY --from=build ${TARGET_DIR}/authentication-service-*.jar /app/runner.jar
EXPOSE 3000
CMD ["java", "-jar", "/app/runner.jar"]