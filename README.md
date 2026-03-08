Spring Boot based ticket purchasing system with comprehensive testing strategy including unit, integration, UI and end-to-end testing.

# Spring Boot Ticket Purchasing System

This project is a web-based **Ticket Purchasing System** developed using **Spring Boot**.  
Users can register, log in, view events, and manage ticket purchases through a web interface.

The application follows a **layered architecture** and focuses heavily on **software testing practices**, including unit, integration, UI, end-to-end, and regression testing.

## Features

- User registration
- User login
- Event listing
- Ticket purchase and cart management
- Duplicate username prevention
- Layered backend architecture

## Architecture

The project is designed using a **layered architecture**:

Controller → Service → Repository → Database


- **Controller Layer**
  Handles HTTP requests and user interactions.

- **Service Layer**
  Contains business logic and validation rules.

- **Repository Layer**
  Manages database operations using Spring Data JPA.

- **View Layer**
  Implemented with **Thymeleaf templates**.

## Technologies Used

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Maven
- Thymeleaf
- H2 In-Memory Database
- JUnit 5
- Mockito
- Selenium WebDriver

## Testing

A comprehensive testing strategy was applied during development.

### Unit Testing
Tests business logic components such as `UserService`.

Tools used:
- JUnit 5
- Mockito

Example scenario:
- Successful user registration
- Duplicate username validation

### Integration Testing
Tests interactions between:

- Controller
- Service
- Repository
- Database

Tools used:
- Spring Boot Test
- MockMvc
- H2 Database

### End-to-End Testing

Simulates real user behavior through a web browser.

Tools used:
- Selenium WebDriver
- ChromeDriver

Example scenario:
- Register → Login → Navigate to Events page

### UI Testing

Tests form behavior and page navigation from the user interface.

Example:
- Register page form submission
- Redirect to login page

### Regression Testing

A regression test suite ensures that previously working features remain functional after changes.

Tests included:
- Service tests
- Repository tests
- Controller tests
- UI tests

## Test Lifecycle (STLC)

The testing process followed the **Software Testing Life Cycle (STLC)**:

1. Requirement Analysis
2. Test Planning
3. Test Design
4. Test Environment Setup
5. Test Execution
6. Test Closure

## Project Structure

```
src
├── main
│   ├── java
│   └── resources
│       ├── static
│       └── templates
└── test
```
## Limitations

Due to project scope limitations, the following tests were not fully implemented:

- Performance testing
- Load testing
- Advanced security testing

## Future Improvements

Potential improvements for the system include:

- Adding performance and load testing
- Expanding security testing
- Integrating automated tests into a CI/CD pipeline
- Cross-browser UI testing

## Author

Gözde Şavkın  
