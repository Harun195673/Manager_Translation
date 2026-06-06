# Management Workflow API

A Spring Boot REST API for managing multilingual workgroups, tasks, and task assignments.

## Overview

Management Workflow API simulates a backend system for organizations where managers work with employees who speak different languages.

A manager can create a task once, select a workgroup, and the system automatically prepares translated task assignments for employees based on their language.

The project demonstrates practical backend development with Spring Boot, JPA relationships, DTOs, validation, global exception handling, scheduled jobs, and unit testing.

## Example Workflow

Imagine a manager wants to send one task to a workgroup with employees who speak different languages.

Instead of manually creating separate task versions, the manager starts one workflow:

1. The manager selects a task and a workgroup.
2. The system loads all employees in that workgroup.
3. Employees are grouped by language.
4. The task message is translated for each language group.
5. Translated task versions are created.
6. Task assignments are generated for the employees.
7. Each assignment can be tracked by deadline and status.

This turns a manual multilingual assignment process into an automated backend workflow.

## Core Features

- Manager, workgroup, employee, and task management
- Task assignment creation and tracking
- Automatic task translation workflow
- Employee grouping by language
- Assignment status tracking: `TODO`, `IN_PROGRESS`, `DONE`, `OVERDUE`
- Bean Validation for request data
- Global exception handling
- Scheduled overdue-task detection
- Unit tests with JUnit and Mockito
- Swagger/OpenAPI documentation

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- Lombok
- Swagger/OpenAPI
- JUnit
- Mockito

## Architecture

The project follows a layered backend architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
