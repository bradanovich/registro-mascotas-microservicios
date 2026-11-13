# ============================================================================
# Dockerfile del microservicio SALUD (puerto 8083)
# Mismo estilo que el resto de los microservicios.
# ============================================================================

# Imagen base: Java 25 (JRE).
FROM eclipse-temurin:25-jre

# Carpeta de trabajo dentro del contenedor.
workdir /app

# Copia el .jar ya compilado (se genera con "mvnw package").
copy target/salud-0.0.1-SNAPSHOT.jar app.jar

# Conexion a MySQL. MySQL NO se dockeriza: queda en tu maquina, que desde
# dentro del contenedor se ve como "host.docker.internal".
env SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/salud_db"
env SPRING_DATASOURCE_USERNAME="root"
env SPRING_DATASOURCE_PASSWORD=""

# Puerto en el que escucha el servicio.
expose 8083

# Comando de arranque del contenedor.
ENTRYPOINT ["java", "-jar", "app.jar"]
