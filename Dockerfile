#1 BUILD
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
# Copiar Maven Wrapper
COPY mvnw .
COPY .mvn .mvn
# Copiar proyecto
COPY pom.xml .
COPY src ./src
# Dar permisos al wrapper
RUN chmod +x mvnw
# Construir proyecto (sin tests)
RUN ./mvnw clean package -DskipTests

# 2 RUN
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copiar solo el JAR resultante
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]