# Library Management API

REST API for a Library Management System built with Spring Boot using layered architecture, Spring Security, and JWT authentication.

## Overview

The project manages a library domain with authentication and authorization support.

Core entities:
- **User**: application users with role-based permissions
- **Author**: a writer who can publish many books
- **Book**: belongs to one author
- **Member**: a library member record

The application follows a layered architecture:

- **controller**: REST API endpoints
- **service**: business logic
- **repository**: database operations
- **entity**: JPA entities
- **dto**: request/response models
- **mapper**: entity and DTO conversion
- **exception**: centralized exception handling
- **config**: application configuration
- **security**: JWT authentication and authorization

---

# Tech Stack

- Java 17+
- Spring Boot 3.3
- Spring Web
- Spring Data JPA
- Spring Security
- JWT Authentication
- BCrypt Password Hashing
- Spring Validation
- SpringDoc OpenAPI / Swagger UI
- MapStruct
- Lombok
- PostgreSQL / MySQL
- H2 for local development

---

# Features

## Authentication

- User registration
- User login
- BCrypt password hashing
- JWT access token generation
- JWT refresh token flow
- Token expiration handling

## Authorization

- Stateless Spring Security configuration
- Role-based access control
- USER and ADMIN roles
- Endpoint protection using Spring Security and `@PreAuthorize`

## Library Management

- CRUD operations for authors, books, and members
- Pagination and sorting
- DTO validation
- Global exception handling
- Service layer unit tests
- Swagger/OpenAPI documentation

---

# Project Structure

```text
src/main/java/librarymanagement

├── controller
├── service
├── repository
├── entity
├── dto
├── mapper
├── exception
├── config
└── security
```

---

# Getting Started

## Prerequisites

- Java 17+
- Gradle
- PostgreSQL or MySQL (optional)

---

# Run Locally

Windows:

```powershell
.\gradlew.bat bootRun
```

Linux/macOS:

```bash
./gradlew bootRun
```

Application starts:

```
http://localhost:8080
```

---

# API Documentation

Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```
http://localhost:8080/v3/api-docs
```

---

# Configuration

Example `application.properties`:

```properties
spring.application.name=library-management

spring.datasource.url=jdbc:postgresql://localhost:5432/library_db
spring.datasource.username=postgres
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


jwt.access-secret=your-access-secret
jwt.refresh-secret=your-refresh-secret

jwt.access-ttl-seconds=900
jwt.refresh-ttl-seconds=604800

jwt.issuer=library-api
jwt.audience=library-client
```

---

# Environment Variables Example

`.env`

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/library_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password

JWT_ACCESS_SECRET=your-access-secret
JWT_REFRESH_SECRET=your-refresh-secret
```

---

# Authentication Flow

## Register

```
POST /api/v1/auth/register
```

Creates a new user.

Password is stored securely using BCrypt hashing.

---

## Login

```
POST /api/v1/auth/login
```

Authenticates user credentials and returns:

```json
{
  "accessToken": "jwt-access-token",
  "refreshToken": "jwt-refresh-token",
  "tokenType": "Bearer"
}
```

---

## Refresh Token

```
POST /api/v1/auth/refresh
```

Creates new tokens using a valid refresh token.

---

# Security Implementation

The application uses Spring Security with JWT authentication.

Authentication flow:

1. User registers or logs in.
2. Server generates access and refresh tokens.
3. Client sends access token:

```
Authorization: Bearer <access_token>
```

4. `JwtAuthenticationFilter` validates the token.
5. SecurityContext is populated.
6. Request continues with authenticated user.

The application uses:

```java
SessionCreationPolicy.STATELESS
```

No server-side sessions are stored.

---

# Role Based Access Control

Available roles:

```java
USER
ADMIN
```

Examples:

ADMIN only:

```
GET /api/v1/admin/users
```

USER and ADMIN:

```
GET /api/v1/users/me
```

Authorization is handled using:

```java
hasRole()
hasAnyRole()
@PreAuthorize
```

---

# Authentication Errors

## 401 Unauthorized

Returned when:

- JWT token is missing
- JWT token is invalid
- JWT token is expired
- User is not authenticated

Handled by:

```
JwtAuthenticationEntryPoint
```

---

## 403 Forbidden

Returned when:

- User is authenticated
- User does not have required permission

Handled by:

```
JwtAccessDeniedHandler
```

---

# API Endpoints

## Authentication

| Method | Endpoint |
|---|---|
| POST | `/api/v1/auth/register` |
| POST | `/api/v1/auth/login` |
| POST | `/api/v1/auth/refresh` |

---

## Users

| Method | Endpoint |
|---|---|
| GET | `/api/v1/users/me` |
| PUT | `/api/v1/users/me` |
| GET | `/api/v1/admin/users` |

---

## Authors

| Method | Endpoint |
|---|---|
| POST | `/api/v1/authors` |
| GET | `/api/v1/authors` |
| GET | `/api/v1/authors/{id}` |
| PUT | `/api/v1/authors/{id}` |
| DELETE | `/api/v1/authors/{id}` |

---

## Books

| Method | Endpoint |
|---|---|
| POST | `/api/v1/books` |
| GET | `/api/v1/books` |
| GET | `/api/v1/books/{id}` |
| PUT | `/api/v1/books/{id}` |
| DELETE | `/api/v1/books/{id}` |

---

## Members

| Method | Endpoint |
|---|---|
| POST | `/api/v1/members` |
| GET | `/api/v1/members` |
| GET | `/api/v1/members/{id}` |
| PUT | `/api/v1/members/{id}` |
| DELETE | `/api/v1/members/{id}` |

---

# Pagination and Sorting

List endpoints support Spring Data pagination:

```
?page=0&size=10&sort=id,asc
```

Example:

```
GET /api/v1/books?page=0&size=5&sort=title,asc
```

---

# Validation and Error Handling

The project includes:

- DTO validation using Jakarta Validation
- Centralized exception handling
- Structured API error responses

Example:

```json
{
  "timestamp": "2026-07-28T10:00:00",
  "status": 400,
  "error": "Validation Failed"
}
```

---

# Testing

Run tests:

Windows:

```powershell
.\gradlew.bat test
```

Linux/macOS:

```bash
./gradlew test
```

Tests include:

- AuthService tests
- UserService tests
- BookService tests

---

# Architecture

The application follows clean layered architecture:

```
Controller
    |
Service
    |
Repository
    |
Database
```

Security flow:

```
Request
   |
JWT Filter
   |
Security Context
   |
Controller
   |
Service
```

---

# Final Notes

This project demonstrates a production-style Spring Boot REST API with:

- Secure authentication
- JWT authorization
- Role-based access control
- Stateless security
- Proper exception handling
- Layered architecture
- Database integration
