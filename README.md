# Library Management API

REST API for a simple library domain built with a layered Spring Boot architecture.

## Overview
The project manages three core entities:
- **Author**: a writer who can publish many books
- **Book**: belongs to one author
- **Member**: a library member record

The codebase is organized into:
- **controller**: REST endpoints
- **service**: business rules
- **repository**: database access
- **entity**: JPA models
- **dto**: request/response models
- **mapper**: MapStruct conversions
- **exception**: centralized error handling
- **config**: application configuration

## Tech stack
- Java 17
- Spring Boot 3.3
- Spring Web
- Spring Data JPA
- Spring Validation
- SpringDoc OpenAPI / Swagger UI
- MapStruct
- Lombok
- H2 for local development
- MySQL / PostgreSQL ready configuration

## Features
- CRUD for authors, books, and members
- Pagination and sorting for list endpoints
- Validation on request DTOs
- Swagger/OpenAPI documentation
- Service-layer unit tests

## Project structure
```text
src/main/java/librarymanagement
├── controller
├── service
├── repository
├── entity
├── dto
├── mapper
├── exception
└── config
```

## Getting started
### Prerequisites
- Java 17+
- Gradle
- Optional: MySQL or PostgreSQL

### Run locally
```powershell
.\gradlew.bat bootRun
```

Or on Linux/macOS:
```bash
./gradlew bootRun
```

The application starts on `http://localhost:8080`.

## API documentation
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Configuration
The app uses H2 by default for local startup, but can be switched to MySQL or PostgreSQL with environment variables.

### `.env` example
```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/library_db
SPRING_DATASOURCE_DRIVER=com.mysql.cj.jdbc.Driver
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=secret
```

### `application.yml` example
```yaml
spring:
  application:
    name: library-management
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:h2:mem:librarydb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL}
    driver-class-name: ${SPRING_DATASOURCE_DRIVER:org.h2.Driver}
    username: ${SPRING_DATASOURCE_USERNAME:sa}
    password: ${SPRING_DATASOURCE_PASSWORD:}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  data:
    web:
      pageable:
        default-page-size: 10
        max-page-size: 50
```

## API endpoints
### Authors
- `POST /api/v1/authors`
- `GET /api/v1/authors`
- `GET /api/v1/authors/{id}`
- `PUT /api/v1/authors/{id}`
- `DELETE /api/v1/authors/{id}`

### Books
- `POST /api/v1/books`
- `GET /api/v1/books`
- `GET /api/v1/books/{id}`
- `PUT /api/v1/books/{id}`
- `DELETE /api/v1/books/{id}`

### Members
- `POST /api/v1/members`
- `GET /api/v1/members`
- `GET /api/v1/members/{id}`
- `PUT /api/v1/members/{id}`
- `DELETE /api/v1/members/{id}`

## Pagination and sorting
List endpoints accept Spring Data query params:
```text
?page=0&size=10&sort=id,asc
```

Examples:
- `GET /api/v1/books?page=0&size=5&sort=title,asc`
- `GET /api/v1/authors?page=1&size=10&sort=lastName,desc`

## Example domain rules
- A book must reference an existing author.
- Email fields are validated and should be unique in the database.
- Invalid requests return structured validation errors.

## Testing
Run tests with:
```powershell
.\gradlew.bat test
```
