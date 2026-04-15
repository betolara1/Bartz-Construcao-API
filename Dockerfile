# Esse é um docker padrão para aplicações Java com Spring Boot

# 1. ESTÁGIO DE BUILD (Construção)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Cache do Maven: Se o pom.xml não mudar, o Docker reutiliza as dependências baixadas
# Copia o pom.xml e baixa as dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copia o resto do código
COPY src ./src
# Compila o projeto
RUN mvn clean package -DskipTests

# 2. ESTÁGIO DE EXECUÇÃO (Rodar o app)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Metadados para ajudar a saberem do que se trata aquela imagem.
LABEL maintainer="Bartz Construção API"

# Define o fuso horário para o container
ENV TZ=America/Sao_Paulo

# Cria um usuário não-root para segurança caso um hacker ataque o sistema
RUN useradd -ms /bin/bash springuser
USER springuser

# Copia o JAR gerado no build para a imagem final
COPY --from=build /app/target/*.jar app.jar

# Porta que o Spring Boot usa (padrão)
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
