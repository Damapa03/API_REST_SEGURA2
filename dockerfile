FROM azul/zulu-openjdk:17-latest
WORKDIR /app
VOLUME /tmp
COPY build/libs/app.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]