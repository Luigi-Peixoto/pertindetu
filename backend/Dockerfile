# Estágio 0: cria usuário não-root
FROM alpine:latest AS security_provider
RUN addgroup -S nonroot && adduser -S nonroot -G nonroot

# Estágio 1: build
FROM maven:3.9.14-eclipse-temurin-25-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Estágio 2: runtime (imagem mínima)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Importa usuário não-root do estágio 0
COPY --from=security_provider /etc/passwd /etc/passwd
COPY --from=security_provider /etc/group  /etc/group

# Copia apenas o .jar gerado
COPY --from=build /app/target/*.jar app.jar

USER nonroot

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]