FROM openjdk:21-ea-24-oracle

WORKDIR /app
COPY target/maestro-0.0.1-SNAPSHOT.jar app.jar
COPY wallet /app/oracle_wallet/
EXPOSE 8082

CMD [ "java", "-jar", "app.jar" ]