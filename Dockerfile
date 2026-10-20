# Compilation de React
FROM node:22-alpine AS frontend-build

WORKDIR /frontend

COPY datapulsewebapp/package.json datapulsewebapp/package-lock.json ./
RUN npm ci

COPY datapulsewebapp/ ./
RUN npm run build


# Compilation de Spring avec les fichiers React
FROM eclipse-temurin:21-jdk AS backend-build

WORKDIR /app

COPY datapulseAPI/.mvn/ .mvn/
COPY datapulseAPI/mvnw datapulseAPI/pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw --batch-mode dependency:go-offline

COPY datapulseAPI/src/ src/

COPY --from=frontend-build /frontend/dist/ src/main/resources/static/

RUN ./mvnw --batch-mode clean package -DskipTests


# Image exécutée sur Cloud Run
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=backend-build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]