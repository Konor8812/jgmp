FROM adoptopenjdk:11-jre-hotspot

WORKDIR /app

COPY target/discovery-1.0-SNAPSHOT.jar artifact.jar

ENTRYPOINT ["java", "-jar", "artifact.jar"]