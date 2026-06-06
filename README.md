# Management Workflow API

A Spring Boot REST API for managing managers, workgroups, employees, tasks, and task assignments in multilingual teams.

## Features

* CRUD operations for Managers, WorkGroups, Employees, and Tasks
* Task assignment tracking
* Status management (TODO, IN_PROGRESS, DONE, OVERDUE)
* Automated task translation workflow
* DTO-based API design
* Bean Validation
* Global Exception Handling
* Scheduled overdue-task detection
* Unit Testing with JUnit and Mockito
* Swagger/OpenAPI documentation

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* H2 Database
* Swagger/OpenAPI
* JUnit & Mockito
* Maven

## Entity Relationship Model

```text
Manager
├── 1:N WorkGroup
├── 1:N Task

WorkGroup
└── 1:N Employee

Employee
└── 1:N TaskAssignment

Task
└── 1:N TaskAssignment
```

## Architecture

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

## Business Workflow

1. Manager creates a task.
2. Employees are grouped by language.
3. Task messages are translated automatically.
4. Translated tasks are generated.
5. Task assignments are created for employees.

## Exception Handling

| Exception                   | HTTP Status |
| --------------------------- | ----------- |
| ResourceNotFoundException   | 404         |
| DuplicateResourceException  | 409         |
| InvalidOperationException   | 400         |
| BusinessValidationException | 422         |
| ForbiddenOperationException | 403         |

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```
