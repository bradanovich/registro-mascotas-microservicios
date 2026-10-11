# ============================================================================
# Dockerfile del EUREKA SERVER (servidor de descubrimiento, puerto 8761)
# Mismo estilo que usuarios/Dockerfile.
# ============================================================================

# Imagen base: Java 25 (JRE), igual que el resto de los microservicios.
FROM eclipse-temurin:25-jre

# Carpeta de trabajo dentro del contenedor.
workdir /app

# Copia el .jar ya compilado (se genera con "mvnw package") dentro de la imagen.
copy target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

# Documenta que el servicio escucha en el puerto 8761.
expose 8761

# Comando de arranque del contenedor.
ENTRYPOINT ["java", "-jar", "app.jar"]
