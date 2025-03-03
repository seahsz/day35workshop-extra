# Build Angular
FROM node:23 AS ng-builder

WORKDIR /ngSrc

RUN npm i -g @angular/cli

COPY client/public public
COPY client/src src
COPY client/*.json .

RUN npm ci && ng build

# Build Spring Boot
FROM openjdk:23-jdk AS j-builder

WORKDIR /javaSrc

COPY server/.mvn .mvn
COPY server/src src
COPY server/mvnw .
COPY server/pom.xml .

# Copy Angular files over to static
# Remove the * form browser/* because * only copies direct files, not the subdirectories
COPY --from=ng-builder /ngSrc/dist/client/browser/ src/main/resources/static/

RUN chmod a+x mvnw && ./mvnw package -Dmaven.test.skip=true

# Copy the JAR file over to the final container
FROM openjdk:23-jdk

WORKDIR /myapp

COPY --from=j-builder /javaSrc/target/day35workshopExtra-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080
ENV SPRING_DATA_REDIS_HOST=
ENV SPRING_DATA_REDIS_PORT=
ENV SPRING_DATA_REDIS_DATABASE=
ENV SPRING_DATA_REDIS_USERNAME=
ENV SPRING_DATA_REDIS_PASSWORD=

ENV OPENWEATHERMAP_API_KEY=

EXPOSE ${PORT}

SHELL [ "/bin/sh", "-c" ]
ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar