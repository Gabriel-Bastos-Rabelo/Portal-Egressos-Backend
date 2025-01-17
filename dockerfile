# Usar uma imagem oficial do OpenJDK como base
FROM openjdk:23

# Definir o diretório de trabalho no contêiner
WORKDIR /app

# Copiar o arquivo JAR da aplicação para o contêiner
COPY target/*.jar app.jar

# Expor a porta usada pelo Spring Boot
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]

