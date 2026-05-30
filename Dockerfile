FROM amazoncorretto:21
WORKDIR /app
COPY target/BattleshipGamePlayer-2.0.jar app.jar
ENTRYPOINT ["java", "-Dlog4j2.loggerContextFactory=org.apache.logging.log4j.simple.SimpleLoggerContextFactory", "-jar", "app.jar"]