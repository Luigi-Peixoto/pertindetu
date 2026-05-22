# Estágio 1: Construção (Build)
FROM maven:3.9.6-eclipse-temurin-22 AS builder
WORKDIR /app
# Copia o pom.xml e baixa as dependências (ajuda no cache do Docker)
COPY pom.xml .
RUN mvn dependency:go-offline
# Copia o código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução (Run)
FROM eclipse-temurin:22-jre-alpine
WORKDIR /app
# Copia apenas o arquivo .jar gerado no estágio 1
COPY --from=builder /app/target/*.jar app.jar
# Copia o arquivo .env para dentro do container
COPY .env .env

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]