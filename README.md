# Expense Tracker Backend

## Descripción General

Este proyecto es el backend para una aplicación de seguimiento de gastos. Permite a los usuarios registrar y gestionar sus gastos, asociándolos a categorías predefinidas. Inicialmente, el sistema contaba con autenticación de usuarios y la posibilidad de que estos crearan sus propias categorías, pero fue simplificado para enfocarse en la funcionalidad principal de registro de gastos con categorías fijas.

## Tecnologías Utilizadas

*   **Java**: Lenguaje de programación principal (JDK 17+).
*   **Spring Boot**: Framework para la creación de aplicaciones Java robustas y auto-configurables.
*   **Spring Security**: Para la gestión de autenticación y autorización (simplificada en la versión actual).
*   **JWT (JSON Web Tokens)**: Utilizado inicialmente para la autenticación basada en tokens.
*   **Maven**: Herramienta para la gestión de dependencias y construcción del proyecto.
*   **MySQL**: Sistema de gestión de bases de datos relacional.
*   **Spring Data JPA**: Para la interacción con la base de datos.
*   **Springdoc OpenAPI (Swagger)**: Para la documentación automática de la API REST.

## Configuración y Puesta en Marcha

### Prerrequisitos

*   JDK 17 o superior.
*   Maven 3.6 o superior.
*   MySQL Server.

### Pasos para la Instalación

1.  **Clonar el repositorio:**
    ```bash
    git clone <url-del-repositorio>
    cd expense-tracker-backend
    ```

2.  **Configurar la base de datos:**
    *   Asegúrate de tener MySQL instalado y en ejecución.
    *   Crea una base de datos en MySQL (ej. `expense_tracker_db`).
    *   Actualiza el archivo `src/main/resources/application.properties` con los detalles de tu conexión a MySQL:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
        spring.datasource.username=tu_usuario_mysql
        spring.datasource.password=tu_contraseña_mysql
        spring.jpa.hibernate.ddl-auto=update # 'create' para la primera ejecución si las tablas no existen, 'update' para posteriores.
        spring.jpa.show-sql=true # Opcional: para ver las queries SQL en consola

        # Configuración de Springdoc OpenAPI (Swagger)
        springdoc.api-docs.path=/v3/api-docs
        springdoc.swagger-ui.path=/swagger-ui.html
        ```

3.  **Construir y ejecutar la aplicación:**
    *   Puedes construir el proyecto y ejecutar las pruebas con:
        ```bash
        ./mvnw clean install
        ```
    *   Para ejecutar la aplicación:
        ```bash
        ./mvnw spring-boot:run
        ```
    La aplicación estará disponible en `http://localhost:8080`.

## Características Principales

*   **Gestión de Gastos:**
    *   Crear nuevos gastos, especificando descripción, monto, fecha y categoría.
    *   Consultar la lista de gastos.
    *   Actualizar gastos existentes.
    *   Eliminar gastos.
    *   *Nota: La autenticación de usuario para la gestión de gastos fue simplificada/eliminada en las últimas fases del desarrollo para facilitar las pruebas.*
*   **Categorías Fijas:**
    *   El sistema incluye un conjunto de categorías fijas predefinidas que se crean al iniciar la aplicación: "Hogar", "Ocio", "Mascotas", "Salud", "Otros", "Ahorros". Estas categorías son globales y no pertenecen a ningún usuario específico.
    *   Al crear un gasto, se debe especificar una de estas categorías por su nombre (insensible a mayúsculas/minúsculas).
*   **API Documentada con Swagger:**
    *   La documentación interactiva de la API está disponible en `http://localhost:8080/swagger-ui.html` una vez que la aplicación está en ejecución.
    *   Permite explorar y probar los diferentes endpoints de la aplicación.

## Endpoints de la API (Principales)

*   **Autenticación (Considerar como deshabilitada o no esencial para las funcionalidades actuales):**
    *   `POST /api/auth/register`: (Ruta original para registrar un nuevo usuario).
    *   `POST /api/auth/login`: (Ruta original para iniciar sesión).

*   **Categorías:**
    *   `GET /api/categories`: Obtener la lista de categorías fijas.
        *   Este endpoint es público y no requiere autenticación.

*   **Gastos:**
    *   `POST /api/expenses`: Crear un nuevo gasto.
        *   Payload esperado (ejemplo):
            ```json
            {
              "description": "Libro de programación",
              "amount": 25.99,
              "date": "2024-07-31",
              "categoryName": "Ocio"
            }
            ```
    *   `GET /api/expenses`: Obtener todos los gastos.
    *   `GET /api/expenses/{id}`: Obtener un gasto específico por ID.
    *   `PUT /api/expenses/{id}`: Actualizar un gasto existente por ID.
    *   `DELETE /api/expenses/{id}`: Eliminar un gasto por ID.

## Resumen del Proceso de Desarrollo y Depuración

El desarrollo de este backend implicó varios ciclos de iteración y depuración, enfocados en:

1.  **Configuración Inicial de Swagger:** Se realizaron ajustes en `SecurityConfig.java` para permitir el acceso público a los paths de Swagger (`/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`).
2.  **Autenticación y Autorización con JWT:** Se implementó un sistema de autenticación basado en JWT (`JwtAuthFilter`, `UserDetailsServiceImpl`). Sin embargo, para simplificar y agilizar las pruebas de la funcionalidad principal (CRUD de gastos), los requisitos de autenticación para ciertos endpoints (como categorías y luego gastos) fueron relajados o eliminados.
3.  **Gestión de Categorías:** Se evolucionó de un sistema donde los usuarios podían crear sus propias categorías a un modelo con un conjunto fijo de categorías globales ("Hogar", "Ocio", etc.), creadas al inicio de la aplicación. Esto simplificó la lógica de creación de gastos, ya que las categorías se buscan por nombre (insensible a mayúsculas/minúsculas) dentro de este conjunto fijo. El CRUD de categorías fue eliminado, dejando solo la consulta de las categorías fijas.
4.  **Lógica de Creación de Gastos:** Se refinó el servicio `ExpenseServiceImpl` para buscar categorías por nombre de manera robusta, manejando la insensibilidad a mayúsculas/minúsculas y asegurando que solo se usen categorías válidas. Se abordaron `NullPointerExceptions` relacionadas con nombres de categorías nulos en la base de datos.
5.  **Limpieza de Base de Datos:** Fue necesario realizar operaciones de limpieza en la tabla `categories` de MySQL para eliminar datos inconsistentes (categorías duplicadas o con nombres nulos) que causaban errores en la aplicación.

Este README provee una visión general del estado del proyecto y los pasos clave en su desarrollo. 