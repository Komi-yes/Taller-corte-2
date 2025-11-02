# API de Gestión de Recetas

Una API RESTful para gestionar recetas con diferentes tipos de chef (Participante, Jurado, Espectador) construida con Spring Boot y MongoDB.

## Tabla de Contenidos
- [Características](#características)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Ejecución de la Aplicación](#ejecución-de-la-aplicación)
- [Documentación de la API](#documentación-de-la-api)
- [Ejemplos](#ejemplos)
  - [Crear una Receta](#crear-una-receta)
  - [Obtener Todas las Recetas](#obtener-todas-las-recetas)
  - [Obtener Receta por ID](#obtener-receta-por-id)
  - [Actualizar Receta](#actualizar-receta)
  - [Eliminar Receta](#eliminar-receta)
- [Pruebas](#pruebas)
- [Despliegue](#despliegue)

## Características
- Crear, leer, actualizar y eliminar recetas
- Soporte para diferentes tipos de chef (Participante, Jurado, Espectador)
- Buscar recetas por ingrediente o temporada
- Validación de entrada y manejo de errores
- Documentación completa de la API con Swagger
- Pruebas unitarias y de integración

## Tecnologías
- Java 17
- Spring Boot 3.2.0
- Spring Data MongoDB
- Lombok
- MapStruct
- JUnit 5
- Mockito
- Jacoco (Cobertura de código)
- Swagger/OpenAPI 3.0

## Requisitos Previos
- Java 17 o superior
- Maven 3.6.3 o superior
- MongoDB 4.4 o superior
- Git

## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tuusuario/taller-corte-2.git
   cd taller-corte-2
   ```

2. Construye el proyecto:
   ```bash
   mvn clean install
   ```

## Ejecución de la Aplicación

1. Inicia el servicio de MongoDB

2. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```

   La aplicación estará disponible en `http://localhost:8080/api/swagger-ui/index.html`

## Documentación de la API

### Interfaz de Usuario de Swagger
Accede a la documentación interactiva de la API en:
```
http://localhost:8080/swagger-ui.html
```

### Especificación OpenAPI
```
http://localhost:8080/v3/api-docs
```

## Ejemplos

### Crear una Receta
**Request:**
```http
POST /api/recipes
Content-Type: application/json

{
  "recipeTitle": "Pasta Carbonara",
  "ingredients": ["pasta", "eggs", "bacon", "parmesan", "black pepper"],
  "instructions": ["Cook pasta", "Fry bacon", "Mix eggs and cheese", "Combine all ingredients"],
  "chefName": "Chef Mario",
  "season": "Summer",
  "chefType": "PARTICIPANT"
}
```

**Response:**
```json
{
  "id": "1",
  "recipeTitle": "Pasta Carbonara",
  "ingredients": ["pasta", "eggs", "bacon", "parmesan", "black pepper"],
  "instructions": ["Cook pasta", "Fry bacon", "Mix eggs and cheese", "Combine all ingredients"],
  "chefName": "Chef Mario",
  "chefType": "PARTICIPANT",
  "season": "Summer"
}
```

### Obtener Todas las Recetas
**Request:**
```http
GET /api/recipes
```

**Response:**
```json
[
  {
    "id": "1",
    "recipeTitle": "Pasta Carbonara",
    "ingredients": ["pasta", "eggs", "bacon", "parmesan", "black pepper"],
    "instructions": ["Cook pasta", "Fry bacon", "Mix eggs and cheese", "Combine all ingredients"],
    "chefName": "Chef Mario",
    "chefType": "PARTICIPANT",
    "season": "Summer"
  }
]
```

### Obtener Receta por ID
**Request:**
```http
GET /api/recipes/1
```

**Response:**
```json
{
  "id": "1",
  "recipeTitle": "Pasta Carbonara",
  "ingredients": ["pasta", "eggs", "bacon", "parmesan", "black pepper"],
  "instructions": ["Cook pasta", "Fry bacon", "Mix eggs and cheese", "Combine all ingredients"],
  "chefName": "Chef Mario",
  "chefType": "PARTICIPANT",
  "season": "Summer"
}
```

### Actualizar Receta
**Request:**
```http
PUT /api/recipes/1
Content-Type: application/json

{
  "recipeTitle": "Pasta Carbonara Deluxe",
  "ingredients": ["pasta", "eggs", "bacon", "parmesan", "black pepper", "garlic"],
  "instructions": ["Cook pasta", "Fry bacon", "Sauté garlic", "Mix eggs and cheese", "Combine all ingredients"],
  "chefName": "Chef Mario Batali"
}
```

**Response:**
```json
{
  "id": "1",
  "recipeTitle": "Pasta Carbonara Deluxe",
  "ingredients": ["pasta", "eggs", "bacon", "parmesan", "black pepper", "garlic"],
  "instructions": ["Cook pasta", "Fry bacon", "Sauté garlic", "Mix eggs and cheese", "Combine all ingredients"],
  "chefName": "Chef Mario Batali",
  "chefType": "PARTICIPANT",
  "season": "Summer"
}
```

### Eliminar Receta
**Request:**
```http
DELETE /api/recipes/1
```

**Response:**
```json
{
  "id": "1",
  "recipeTitle": "Pasta Carbonara Deluxe",
  "ingredients": ["pasta", "eggs", "bacon", "parmesan", "black pepper", "garlic"],
  "instructions": ["Cook pasta", "Fry bacon", "Sauté garlic", "Mix eggs and cheese", "Combine all ingredients"],
  "chefName": "Chef Mario Batali",
  "chefType": "PARTICIPANT",
  "season": "Summer"
}
```

## Pruebas

Ejecutar todas las pruebas y generar informe de cobertura de pruebas:
```bash
  mvn test jacoco:report
```
El informe estará disponible en `target/site/jacoco/index.html`

## Despliegue

### Requisitos para el Despliegue en Azure
- Cuenta de Azure
- Azure CLI instalado
- Extensión de Azure App Service para VS Code o acceso al Portal de Azure

### Desplegar en Azure App Service
1. Empaqueta la aplicación:
   ```bash
   mvn clean package
   ```

2. Despliega usando Azure CLI:
   ```bash
   az webapp up --resource-group TuGrupoDeRecursos --name TuNombreDeAplicacion --runtime JAVA:17 --sku B1
   ```

3. Configura las variables de entorno en el Portal de Azure:
   - `SPRING_DATA_MONGODB_URI`: Tu cadena de conexión de MongoDB
   - `SPRING_PROFILES_ACTIVE`: prod

### Link Azure

https://masterchefpalaciosapi-evdhgjchhceweyaq.canadacentral-01.azurewebsites.net/api/swagger-ui/index.html