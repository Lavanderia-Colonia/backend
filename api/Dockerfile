# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copia arquivos do Maven wrapper
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd . 
COPY .mvn .mvn

# Baixa dependências (cache)
RUN mvn -q -e -B dependency:go-offline

# Copia o código fonte
COPY src ./src

# Compila a aplicação
RUN mvn -q -e -B clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Cria um usuário não-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xmx512m -Xms256m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
