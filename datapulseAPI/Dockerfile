# Étape 1 : compilation de l'application
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copie du Maven Wrapper
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Autorise l'exécution du wrapper Maven dans Linux
RUN chmod +x mvnw

# Télécharge les dépendances Maven
RUN ./mvnw dependency:go-offline

# Copie du code source
COPY src src

# Compile l'application et génère le fichier JAR
RUN ./mvnw clean package -DskipTests


# Étape 2 : exécution de l'application
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copie uniquement le JAR produit à l'étape précédente
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]