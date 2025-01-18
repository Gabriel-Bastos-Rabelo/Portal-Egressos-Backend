# Usar uma imagem oficial do OpenJDK como base
FROM openjdk:23

# Definir o diretório de trabalho no contêiner
WORKDIR /app

# Copiar o arquivo JAR da aplicação para o contêiner
COPY target/portal_egressos_backend-0.0.1-SNAPSHOT.jar app.jar

# Copiar o arquivo .env para o contêiner
COPY .env /app/.env

# Expor a porta usada pelo Spring Boot
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]




