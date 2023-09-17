#
# Build stage
#
FROM openjdk:17-alpine AS build
COPY src /uservault/src
COPY pom.xml /uservault
# RUN mvn -f /uservault/pom.xml clean package

# Instala o Maven
RUN apk --no-cache add maven

CD uservault
RUN mvn clean package
#
# Package stage
#
FROM openjdk:17-alpine
COPY --from=build /target/uservault-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]

# FROM openjdk:17-alpine
#
# WORKDIR /app
#
# COPY target/uservault-0.0.1-SNAPSHOT.jar app.jar
#
# ENTRYPOINT ["java", "-jar", "app.jar"]
