# Management Workflow API

A Spring Boot REST API for managing multilingual workgroups, tasks, task assignments, and authenticated users.

---

## Live Demo

**Swagger UI:**
[Open API Documentation](https://manager-translation.onrender.com/swagger-ui/index.html#/employee-controller/getAllEmployees)

---

## Overview

Management Workflow API is a Spring Boot backend application that automates task distribution for multilingual teams.

Managers can create workgroups, add employees, create tasks, and automatically assign translated task versions to employees based on their language.

The application also includes Spring Security with user accounts for managers and employees. Each manager or employee is connected to a `WebUser` account through a one-to-one relationship.

Example workflow:

> A manager logs in → creates one task → the system groups employees by language → translates the task → creates translated task versions → assigns them to the correct employees.

---

## Entity Relationship Model

```mermaid
erDiagram
    WEB_USER ||--|| MANAGER : account_for
    WEB_USER ||--|| EMPLOYEE : account_for

    MANAGER ||--o{ WORKGROUP : manages
    MANAGER ||--o{ TASK : creates
    WORKGROUP ||--o{ EMPLOYEE : contains
    EMPLOYEE ||--o{ TASK_ASSIGNMENT : receives
    TASK ||--o{ TASK_ASSIGNMENT : assigned_through

    WEB_USER {
        Long id
        String username
        String password
        String role
    }

    MANAGER {
        Long id
        String name
        Long web_user_id
    }

    WORKGROUP {
        Long id
        String name
        Long manager_id
    }

    EMPLOYEE {
        Long id
        String name
        Language language
        Long workgroup_id
        Long web_user_id
    }

    TASK {
        Long id
        String title
        String message
        LocalDate createdDateTask
        Long manager_id
    }

    TASK_ASSIGNMENT {
        Long id
        String name
        LocalDate deadline
        int hoursWorked
        Status status
        Long employee_id
        Long task_id
    }
```

---

## Example Workflow

Imagine a manager needs to send one task to a workgroup that contains:

* Turkish-speaking employees
* Arabic-speaking employees
* Polish-speaking employees
* French-speaking employees

Instead of manually translating the task and assigning it several times, the manager starts one workflow.

The system then:

1. Authenticates the manager.
2. Loads the selected workgroup.
3. Retrieves all employees in that workgroup.
4. Groups employees by language.
5. Translates the task message for each language.
6. Creates translated task versions.
7. Creates task assignments for the employees.
8. Tracks each assignment by deadline and status.

This turns a manual multilingual assignment process into an automated backend workflow.

---

## Core Features

* CRUD operations for managers, workgroups, employees, tasks, and assignments
* Spring Security authentication
* BCrypt password encryption
* User accounts with roles
* One-to-one relationship between `WebUser` and `Manager`
* One-to-one relationship between `WebUser` and `Employee`
* Role-based structure for managers and employees
* Automated multilingual task assignment workflow
* Employee grouping by language
* Assignment status tracking: `TODO`, `IN_PROGRESS`, `DONE`, `OVERDUE`
* Bean Validation and global exception handling
* Scheduled overdue-task detection
* Unit tests with JUnit and Mockito
* Swagger/OpenAPI documentation

---

## Tech Stack

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* H2 Database
* Maven
* Lombok
* Swagger/OpenAPI
* JUnit
* Mockito

---

## Architecture

The application follows a layered backend architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

This structure separates API handling, business logic, persistence logic, and database access.

Security is handled through Spring Security, with authenticated `WebUser` accounts connected to business entities such as `Manager` and `Employee`.
