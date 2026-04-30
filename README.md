# 🚀 Cliente API - Spring Boot 4.0.5

Esta es mi primera **API REST** profesional construida con Java y Spring Boot. El proyecto ha evolucionado de una configuración local simple a una arquitectura **containerizada** con Docker, aplicando buenas prácticas de seguridad, persistencia de datos y gestión de entornos.

## 🛠️ Tecnologías y Herramientas
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 4.0.5
* **Infraestructura:** **Docker & Docker Compose**
* **Base de Datos:** **PostgreSQL 17**
* **Gestor de Dependencias:** Maven
* **Arquitectura:** Multicapa (Controller, Service, Repository)
* **Documentación:** Swagger / OpenAPI 3.0

## 📦 Características (Features)
- **Infraestructura como Código:** Despliegue automático de la base de datos mediante Docker.
- **Seguridad de Entorno:** Gestión de credenciales mediante archivos `.env` (protegidos mediante `.gitignore`).
- **CRUD Completo:** Creación, lectura, actualización y eliminación de clientes.
- **Validación de Datos:** Uso de `jakarta-validation` para asegurar integridad.
- **Manejo Global de Errores:** Respuestas JSON estandarizadas para errores 400 y 404.
- **Documentación Interactiva:** Swagger UI integrado para pruebas rápidas de endpoints.

## ⚙️ Configuración y Ejecución

### 1. Requisitos previos
* Docker Desktop instalado y en ejecución.
* Java 21 y Maven.dfg


### 2. Variables de Entorno
Crea un archivo `.env` en la raíz del proyecto (este archivo está configurado para ser ignorado por Git por seguridad) con el siguiente formato:
```env
DB_NAME=nombre_de_tu_bd
DB_USER=tu_usuario_de_bd
DB_PASSWORD=tu_password_segura.
```

### 3. Levantar la infraestructura

Para iniciar la basde Docker, ejecuta el siguiente comando en la terminal:

```docker compose up -d```

### 4. Ejecutar la aplicación

Una vez que el contenedor de PostgreSQL esté corriendo, puedes lanzar la API desde tu IDE (IntelliJ IDEA) o mediante la terminal:

```mvn spring-boot:run```

La API estará disponible en: http://localhost:8080  
La documentación Swagger en: http://localhost:8080/swagger-ui.html

### 🗺️ Próximas Actualizaciones (Roadmap)
Este proyecto sigue en desarrollo activo. Las próximas fases de implementación incluyen:

[ ] Seguridad: Implementación de autenticación y autorización mediante JWT.





