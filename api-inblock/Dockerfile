#Maven
FROM maven:3.6.0-alpine AS MAVEN_CONTAINER
WORKDIR /usr/src/app
COPY pom.xml .
COPY src src
RUN mvn package

# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine
WORKDIR /usr/src/app
# Copy war file
COPY --from=MAVEN_CONTAINER /usr/src/app/target/inBLOCK-API-0.0.1-SNAPSHOT.jar . 
# Copy keystore
COPY keystore.p12 .
# run the app
CMD ["/usr/bin/java", "-jar", "inBLOCK-API-0.0.1-SNAPSHOT.jar","--server.ssl.enabled=true", "--server.ssl.key-store=keystore.p12","--server.ssl.key-store-password=EYweb22020???", "--server.port=8443"]
