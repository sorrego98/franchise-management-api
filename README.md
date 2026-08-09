# Franchise Management API

API para la gestión de franquicias, desarrollada con **Java 21, Spring
Boot 4.1, Spring WebFlux, Spring Data Reactive MongoDB y MongoDB
Atlas**, siguiendo los principios de **Clean Architecture**.

------------------------------------------------------------------------

## Requisitos

-   Java 21
-   Git
-   Gradle Wrapper incluido en el proyecto
-   MongoDB local o MongoDB Atlas
-   Docker, si se desea ejecutar mediante contenedor

------------------------------------------------------------------------

# Referencia

El proyecto se construye a partir del scaffold de Clean Architecture
de Bancolombia y mantiene la separación entre dominio, casos de uso,
adapters e infraestructura.

[Scaffold CLean Architecture]([https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a](https://bancolombia.github.io/scaffold-clean-architecture/docs/getting-started/))

------------------------------------------------------------------------

# Persistencia

El flujo de persistencia es:

``` text
API
 |
 | SPRING_DATA_MONGODB_URI
 v
MongoDB Atlas
 |
 v
franchise_db
```

La aplicación utiliza repositorios reactivos de Spring Data MongoDB.

------------------------------------------------------------------------

# Endpoints

La API expone operaciones relacionadas con:

-   Franquicias
-   Sucursales
-   Productos
-   Stock

Los entry points están implementados en:

``` text
infrastructure/entry-points/reactive-web
```

La lógica de negocio se encuentra en:

``` text
domain/usecase
```

La persistencia se encuentra en:

``` text
infrastructure/driven-adapters/mongo-repository
```

------------------------------------------------------------------------

# Flujo de una operación

``` text
HTTP Request
     |
     v
Handler
     |
     v
Use Case
     |
     v
Domain
     |
     v
Repository Port
     |
     v
MongoDB Adapter
     |
     v
MongoDB Atlas
```

Esto mantiene desacoplados los casos de uso de la tecnología específica
utilizada para persistencia.

------------------------------------------------------------------------

# Pruebas

Ejecutar pruebas:

``` bash
gradle test
```
------------------------------------------------------------------------

# Configuración

La aplicación utiliza la propiedad:

``` text
SPRING_DATA_MONGODB_URI
```

En `application.yaml` se utiliza un valor por defecto para desarrollo
local:

``` yaml
spring:
  data:
    mongodb:
      uri: ${SPRING_DATA_MONGODB_URI:mongodb://localhost:27017/franchise_db}
```

Esto permite utilizar MongoDB local cuando la variable no existe y
MongoDB Atlas cuando está definida.

------------------------------------------------------------------------

### MongoDB Atlas

La persistencia utiliza un cluster gratuito de MongoDB Atlas:

-   Provider: AWS
-   Región: US East 1
-   Tier: Free
-   Base de datos: `franchise_db`

Para ejecutar localmente contra Atlas, en Git Bash:

``` bash
export SPRING_DATA_MONGODB_URI="mongodb+srv://<usuario>:<password>@<cluster>.mongodb.net/franchise_db?retryWrites=true&w=majority&appName=franchise-cluster"
```
------------------------------------------------------------------------
# Ejecución

## Casos de ejecución

### Caso 1: Proyecto local + Mongo Docker

Levantar unicamente el servicio de MongoDB:

``` bash
docker compose up -d mongodb
```
Compilar y ejecutar el proyecto en local:

``` bash
gradle clean build
gradle bootRun
```

La API queda disponible en:

``` text
http://localhost:8080
```

Detener el servicio de MongoDB:

``` text
docker compose stop mongodb
```

### Caso 2: Proyecto Docker  + Mongo Docker

Construir las imagenes de Docker y levantar los servicios:

``` text
docker compose up -d --build
```
En caso de que las imagenes ya estén contruidas:

``` text
docker compose up -d
```

La API queda disponible en:

``` text
http://localhost:8080
```

Detener y eliminar los contenedores de Docker:

``` text
docker compose down
```

Para eliminar los contenedores y la persistencia de MongoDB:

```bash
docker compose down -v
```

### Caso 3: Proyecto Docker  + Mongo Atlas

Este escenario ejecuta la aplicación dentro de un contenedor Docker y 
utiliza MongoDB Atlas como servicio de persistencia.

Construcción de la imagen del proyecto:

```bash
docker build -t franchise-management-api:latest .
```

Se define la URI de Mongo Atlas:

```bash
MONGODB_URI='mongodb+srv://<user>:<password>@...'
```

Ejecutar contenedor:

```bash
docker run --rm -p 8080:8080 \
  -e "SPRING_DATA_MONGODB_URI=$MONGODB_URI" \
  franchise-management-api
```

La API queda disponible en:

``` text
http://localhost:8080
```
------------------------------------------------------------------------

# Terraform

Terraform se utiliza para gestionar la infraestructura relacionada con
MongoDB Atlas.

``` text
terraform/
├── main.tf
├── variables.tf
└── versions.tf
```

Las credenciales de Terraform no se almacenan en el repositorio.

En Git Bash:

``` bash
export MONGODB_ATLAS_CLIENT_ID="<client-id>"
export MONGODB_ATLAS_CLIENT_SECRET="<client-secret>"
```

Comandos:

``` bash
terraform -chdir=terraform init
terraform -chdir=terraform fmt
terraform -chdir=terraform validate
terraform -chdir=terraform plan
```

El `plan` debe revisarse antes de aplicar cambios.

Los archivos sensibles y el estado local de Terraform están excluidos
mediante `.gitignore`.

------------------------------------------------------------------------

# Docker

La aplicación se empaqueta como una imagen Docker.

Flujo:

``` text
Código
  |
  v
Gradle
  |
  v
JAR
  |
  v
Docker
  |
  v
franchise-management-api
```

Construcción:

``` bash
docker build -t franchise-management-api:latest .
```

Ejecución local:

``` bash
docker run --rm   -p 8080:8080   -e SPRING_DATA_MONGODB_URI="$SPRING_DATA_MONGODB_URI"   franchise-management-api:latest
```
------------------------------------------------------------------------

# Seguridad y secretos

No se almacenan credenciales de MongoDB ni credenciales de Terraform
dentro del código fuente.

Variables utilizadas:

``` text
SPRING_DATA_MONGODB_URI
MONGODB_ATLAS_CLIENT_ID
MONGODB_ATLAS_CLIENT_SECRET
```

Los valores reales deben configurarse mediante variables de entorno o
mecanismos de secretos del entorno de ejecución.

Los archivos sensibles y estados de Terraform están incluidos en
`.gitignore`.



