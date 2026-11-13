\# Registro de Mascotas - Arquitectura de Microservicios



Proyecto académico desarrollado en Duoc UC utilizando una arquitectura de microservicios con Spring Boot y Spring Cloud.



El sistema permite registrar usuarios y mascotas, administrar información de salud, gestionar citas veterinarias y crear recordatorios asociados a las mascotas.



\---



\## Integrantes



\- Xaviera Bradanovich

\- Krishna Aránguiz

\- Ossmar Herrera



\---



\## Arquitectura del sistema



El proyecto está compuesto por cinco microservicios de negocio y dos servicios de infraestructura.



| Servicio | Puerto | Descripción |

|---|---:|---|

| Eureka Server | 8761 | Registro y descubrimiento de servicios |

| API Gateway | 8080 | Punto de entrada al sistema |

| Usuarios MS | 8081 | Gestión de usuarios |

| Mascotas MS | 8082 | Gestión de mascotas |

| Salud MS | 8083 | Fichas clínicas, controles, vacunas y desparasitaciones |

| Citas MS | 8084 | Gestión de citas veterinarias |

| Recordatorios MS | 8085 | Gestión de recordatorios |



\### Flujo general



```text

Cliente / Postman

&#x20;       |

&#x20;       v

&#x20;API Gateway :8080

&#x20;       |

&#x20;       v

&#x20;Eureka Server :8761

&#x20;       |

&#x20;       +--------------------------------------+

&#x20;       |             |          |            |

&#x20;       v             v          v            v

&#x20;Usuarios         Mascotas     Salud        Citas

&#x20; :8081            :8082       :8083        :8084

&#x20;                                 |

&#x20;                                 v

&#x20;                           Recordatorios

&#x20;                              :8085

```



El API Gateway utiliza Eureka y Spring Cloud LoadBalancer para localizar los microservicios mediante sus nombres registrados.



Por ejemplo:



```yaml

uri: lb://mascotas

```



Esto permite que el cliente utilice el puerto `8080` como punto de entrada sin tener que conocer directamente el puerto interno de cada microservicio.



\---



\## Tecnologías utilizadas



\- Java 17

\- Spring Boot

\- Spring Cloud

\- Spring Cloud Gateway

\- Netflix Eureka

\- Spring Cloud LoadBalancer

\- OpenFeign

\- Spring Data JPA

\- Hibernate

\- Resilience4j

\- Maven

\- MySQL

\- Lombok

\- Swagger / OpenAPI

\- Postman

\- Git

\- GitHub



\---



\## Estructura del proyecto



```text

registro-mascotas-microservicios/

│

├── eureka-server/

├── gateway/

├── usuarios/

├── mascotas/

├── salud/

├── citas/

├── recordatorios/

└── README.md

```



Cada microservicio corresponde a un proyecto Spring Boot independiente.



\---



\## Requisitos



Antes de ejecutar el proyecto se necesita:



\- Java 17

\- Git

\- MySQL

\- Maven Wrapper incluido en cada proyecto

\- Postman, recomendado para realizar pruebas



Para el desarrollo local se utilizó MySQL mediante XAMPP.



Configuración utilizada:



```text

Host: localhost

Puerto MySQL: 3306

Usuario: root

Contraseña: vacía

```



\---



\## Clonar el proyecto



Ejecutar:



```powershell

git clone https://github.com/bradanovich/registro-mascotas-microservicios.git

```



Entrar al proyecto:



```powershell

cd registro-mascotas-microservicios

```



\---



\## Crear las bases de datos



Antes de iniciar los microservicios se deben crear las siguientes bases de datos en MySQL:



```sql

CREATE DATABASE IF NOT EXISTS usuarios\_db;

CREATE DATABASE IF NOT EXISTS mascotas\_db;

CREATE DATABASE IF NOT EXISTS salud\_db;

CREATE DATABASE IF NOT EXISTS citas\_db;

CREATE DATABASE IF NOT EXISTS recordatorios\_db;

```



No es necesario crear manualmente las tablas.



Hibernate las genera automáticamente gracias a la configuración:



```yaml

spring:

&#x20; jpa:

&#x20;   hibernate:

&#x20;     ddl-auto: update

```



\---



\# Ejecución del sistema



\## 1. Iniciar Eureka Server



Abrir una terminal:



```powershell

cd eureka-server

.\\mvnw.cmd spring-boot:run

```



Eureka estará disponible en:



```text

http://localhost:8761

```



\---



\## 2. Iniciar Usuarios MS



Abrir otra terminal:



```powershell

cd usuarios

.\\mvnw.cmd spring-boot:run

```



Puerto:



```text

8081

```



\---



\## 3. Iniciar Mascotas MS



```powershell

cd mascotas

.\\mvnw.cmd spring-boot:run

```



Puerto:



```text

8082

```



\---



\## 4. Iniciar Salud MS



```powershell

cd salud

.\\mvnw.cmd spring-boot:run

```



Puerto:



```text

8083

```



\---



\## 5. Iniciar Citas MS



```powershell

cd citas

.\\mvnw.cmd spring-boot:run

```



Puerto:



```text

8084

```



\---



\## 6. Iniciar Recordatorios MS



```powershell

cd recordatorios

.\\mvnw.cmd spring-boot:run

```



Puerto:



```text

8085

```



\---



\## 7. Iniciar API Gateway



Finalmente:



```powershell

cd gateway

.\\mvnw.cmd spring-boot:run

```



Puerto:



```text

8080

```



\---



\## Verificar los servicios en Eureka



Ingresar a:



```text

http://localhost:8761

```



Los servicios deberían aparecer registrados con estado:



```text

UP

```



Por ejemplo:



```text

GATEWAY

USUARIOS

MASCOTAS

SALUD

CITAS

RECORDATORIOS

```



\---



\# API Gateway



El Gateway funciona como punto de entrada principal al sistema.



Las solicitudes pueden realizarse utilizando:



```text

http://localhost:8080

```



Por ejemplo:



```http

GET http://localhost:8080/mascotas-app/mascotas/3

```



El flujo es:



```text

Postman

&#x20;  |

&#x20;  v

Gateway :8080

&#x20;  |

&#x20;  v

Eureka

&#x20;  |

&#x20;  v

Mascotas MS :8082

&#x20;  |

&#x20;  v

MySQL

```



\---



\# Microservicio Usuarios



Puerto:



```text

8081

```



Ruta base:



```text

/mascotas-app/usuarios

```



Principales endpoints:



```http

POST   /mascotas-app/usuarios/registro

POST   /mascotas-app/usuarios/login

GET    /mascotas-app/usuarios

GET    /mascotas-app/usuarios/{id}

PUT    /mascotas-app/usuarios/{id}

DELETE /mascotas-app/usuarios/{id}

```



Este microservicio permite:



\- Registrar usuarios.

\- Iniciar sesión.

\- Consultar usuarios.

\- Actualizar usuarios.

\- Eliminar usuarios.



\---



\# Microservicio Mascotas



Puerto:



```text

8082

```



Ruta base:



```text

/mascotas-app/mascotas

```



Endpoints principales:



```http

POST   /mascotas-app/mascotas

GET    /mascotas-app/mascotas

GET    /mascotas-app/mascotas/{id}

PUT    /mascotas-app/mascotas/{id}

DELETE /mascotas-app/mascotas/{id}

```



Mascotas MS utiliza OpenFeign para validar que el dueño exista en Usuarios MS.



La mascota almacena:



```java

private Integer idDueno;

```



No se utiliza una relación JPA entre Usuario y Mascota debido a que ambas entidades pertenecen a microservicios y bases de datos diferentes.



La relación se valida mediante comunicación REST.



\---



\# Microservicio Salud



Puerto:



```text

8083

```



Ruta base:



```text

/mascotas-app/salud

```



Salud MS administra cuatro recursos principales:



```text

/fichas-clinicas

/controles

/vacunas

/desparasitaciones

```



Ejemplos:



```http

POST /mascotas-app/salud/fichas-clinicas

GET  /mascotas-app/salud/fichas-clinicas

```



```http

POST /mascotas-app/salud/controles

GET  /mascotas-app/salud/controles

```



```http

POST /mascotas-app/salud/vacunas

GET  /mascotas-app/salud/vacunas

```



```http

POST /mascotas-app/salud/desparasitaciones

GET  /mascotas-app/salud/desparasitaciones

```



Salud MS utiliza OpenFeign para comprobar que una mascota exista en Mascotas MS antes de guardar información relacionada con ella.



Flujo:



```text

Salud MS

&#x20;  |

&#x20;  v

OpenFeign

&#x20;  |

&#x20;  v

Eureka

&#x20;  |

&#x20;  v

Mascotas MS

```



\---



\# Relaciones JPA



Dentro de Salud MS se implementó una relación JPA entre:



```text

FichaClinica

&#x20;     |

&#x20;     | OneToMany

&#x20;     v

&#x20;  Control

```



En `FichaClinica`:



```java

@OneToMany(mappedBy = "fichaClinica", cascade = CascadeType.ALL)

private List<Control> controles;

```



En `Control`:



```java

@ManyToOne(fetch = FetchType.LAZY)

@JoinColumn(name = "idFicha")

private FichaClinica fichaClinica;

```



Esto permite representar una relación real dentro de la base de datos de Salud.



Una ficha clínica puede estar relacionada con varios controles.



La relación es posible porque ambas entidades pertenecen al mismo microservicio y utilizan la misma base de datos.



En cambio, la mascota pertenece a Mascotas MS, por lo que Salud únicamente almacena:



```java

private Integer idMascota;

```



La existencia de esa mascota se valida mediante Feign.



\---



\# Circuit Breaker



Salud MS utiliza Circuit Breaker para la comunicación con Mascotas MS.



La implementación utiliza Resilience4j mediante Spring Cloud Circuit Breaker.



Su objetivo es evitar que una caída de Mascotas MS provoque la caída completa de Salud MS.



\### Funcionamiento normal



```text

Salud MS

&#x20;  |

&#x20;  v

Mascotas MS

&#x20;  |

&#x20;  v

Respuesta correcta

```



\### Cuando Mascotas MS no está disponible



```text

Salud MS

&#x20;  |

&#x20;  v

Circuit Breaker

&#x20;  |

&#x20;  v

Fallback

```



El sistema puede responder:



```text

No fue posible validar la mascota en Mascotas MS.

```



Durante las pruebas se comprobó que Salud MS continuaba funcionando cuando Mascotas MS se encontraba detenido.



También se comprobó que, una vez iniciado nuevamente Mascotas MS, Salud recuperaba la comunicación.



\---



\# Microservicio Citas



Puerto:



```text

8084

```



Ruta base:



```text

/mascotas-app/citas

```



Endpoints:



```http

POST   /mascotas-app/citas

GET    /mascotas-app/citas

GET    /mascotas-app/citas/{id}

PUT    /mascotas-app/citas/{id}

DELETE /mascotas-app/citas/{id}

```



Antes de crear o actualizar una cita, Citas MS valida mediante OpenFeign que la mascota exista en Mascotas MS.



Ejemplo:



```json

{

&#x20; "idMascota": 3,

&#x20; "fecha": "2026-10-10",

&#x20; "hora": "10:30",

&#x20; "motivo": "Control general",

&#x20; "veterinario": "Dra. Rojas",

&#x20; "estado": "PENDIENTE"

}

```



Si la mascota no existe, el servicio responde con un error.



Ejemplo:



```text

La mascota no existe.

```



\---



\# Microservicio Recordatorios



Puerto:



```text

8085

```



Ruta base:



```text

/mascotas-app/recordatorios

```



Endpoints:



```http

POST   /mascotas-app/recordatorios

GET    /mascotas-app/recordatorios

GET    /mascotas-app/recordatorios/{id}

PUT    /mascotas-app/recordatorios/{id}

DELETE /mascotas-app/recordatorios/{id}

```



Ejemplo:



```json

{

&#x20; "idMascota": 3,

&#x20; "titulo": "Vacuna antirrabica",

&#x20; "mensaje": "Luna tiene una vacuna pendiente",

&#x20; "fechaEnvio": "2026-10-10",

&#x20; "estado": "PENDIENTE"

}

```



Antes de crear o actualizar un recordatorio se valida que la mascota exista en Mascotas MS.



El campo `estado` permite representar la situación actual del recordatorio.



Ejemplos:



```text

PENDIENTE

ENVIADO

```



\---



\# Validaciones



Los microservicios utilizan Jakarta Validation.



Se utilizan anotaciones como:



```java

@NotNull

@NotBlank

@Size

@Email

@PastOrPresent

@FutureOrPresent

@DecimalMin

@DecimalMax

@Pattern

```



Estas validaciones permiten evitar el ingreso de datos incorrectos antes de almacenarlos en la base de datos.



\---



\# Códigos HTTP utilizados



La API utiliza códigos HTTP según el resultado de cada operación.



```text

200 OK

```



Operación realizada correctamente.



```text

201 Created

```



Registro creado correctamente.



```text

204 No Content

```



Registro eliminado correctamente.



```text

400 Bad Request

```



Solicitud incorrecta o validación fallida.



```text

404 Not Found

```



Registro no encontrado.



\---



\# Ejemplo de prueba con Postman



\## Consultar una mascota mediante Gateway



```http

GET http://localhost:8080/mascotas-app/mascotas/3

```



Ejemplo de respuesta:



```json

{

&#x20; "idMascota": 3,

&#x20; "nombre": "Luna",

&#x20; "especie": "Perro",

&#x20; "raza": "Golden Retriever",

&#x20; "fechaNacimiento": "2022-05-10",

&#x20; "idDueno": 3

}

```



Esta prueba permite comprobar:



```text

Postman

&#x20;  |

&#x20;  v

Gateway

&#x20;  |

&#x20;  v

Eureka

&#x20;  |

&#x20;  v

Mascotas MS

&#x20;  |

&#x20;  v

MySQL

```



\---



\# Maven



Cada servicio puede compilarse utilizando Maven Wrapper.



Desde la carpeta de un microservicio:



```powershell

.\\mvnw.cmd clean compile

```



Si la compilación es correcta debería aparecer:



```text

BUILD SUCCESS

```



\---



\## Generar el JAR



Ejecutar:



```powershell

.\\mvnw.cmd clean package

```



El JAR generado se encuentra dentro de:



```text

target/

```



Por ejemplo:



```text

mascotas-0.0.1-SNAPSHOT.jar

```



\---



\## Ejecutar el JAR



Ejemplo:



```powershell

java -jar target\\mascotas-0.0.1-SNAPSHOT.jar

```



Cada uno de los servicios fue probado también mediante su JAR ejecutable.



\---



\# Swagger / OpenAPI



Los microservicios poseen configuración de Swagger/OpenAPI para visualizar y documentar los endpoints REST.



Ejemplo para Mascotas MS:



```text

http://localhost:8082/swagger-ui/index.html

```



El puerto debe modificarse dependiendo del servicio que se quiera consultar.



\---



\# Estrategia Git



Para el desarrollo se utiliza una estructura basada en ramas.



```text

main

&#x20;|

develop

&#x20;|

&#x20;+-- feature/usuarios-mascotas-gateway

&#x20;|

&#x20;+-- feature/salud-eureka

&#x20;|

&#x20;+-- feature/citas-recordatorios

&#x20;|

&#x20;+-- feature/documentacion-readme

```



El trabajo se realiza primero en ramas `feature`.



Luego se crea un Pull Request hacia:



```text

develop

```



Cuando todas las funcionalidades estén integradas y verificadas, se realiza la integración final:



```text

develop

&#x20;  |

&#x20;  v

main

```



Se utilizan commits descriptivos para identificar claramente los cambios realizados.



Ejemplos:



```text

feat: implementar microservicio usuarios

feat: implementar microservicio mascotas

feat: configurar api gateway con eureka

feat: implementar microservicio salud

feat: configurar eureka server

feat: implementar microservicio citas

feat: implementar microservicio recordatorios

```



\---



\# Estado del proyecto



El proyecto cuenta con:



\- Usuarios MS implementado.

\- Mascotas MS implementado.

\- Salud MS implementado.

\- Citas MS implementado.

\- Recordatorios MS implementado.

\- Eureka Server configurado.

\- API Gateway configurado.

\- Persistencia con MySQL.

\- CRUD REST.

\- Validaciones.

\- OpenFeign.

\- Service Discovery.

\- Spring Cloud LoadBalancer.

\- Circuit Breaker.

\- Relaciones JPA.

\- Swagger / OpenAPI.

\- Compilación con Maven.

\- Generación de JAR ejecutable.

\- Pruebas con Postman.

\- Control de versiones mediante Git y GitHub.



\---



\## Proyecto académico



Proyecto desarrollado para Duoc UC.



2026

