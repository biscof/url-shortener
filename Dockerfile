FROM gradle:8.0.2-jdk17 as build

WORKDIR /app

COPY . .

RUN ./gradlew bootJar

FROM bellsoft/liberica-openjdk-alpine-musl:17 as main

WORKDIR /app

COPY --from=build /app/build/libs/url-shortener-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "url-shortener-0.0.1-SNAPSHOT.jar"]