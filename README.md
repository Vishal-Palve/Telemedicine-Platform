# Telemedicine Platform API

A production-grade REST API for a Telemedicine Platform, built with Java, Spring Boot, and Docker. This backend service powers an application that connects patients with doctors for virtual consultations.

[![Java CI with Maven](https://github.com/Vishal-Palve/Telemedicine-Platform/actions/workflows/build.yml/badge.svg)](https://github.com/Vishal-Palve/Telemedicine-Platform/actions/workflows/build.yml)

---

## Live Demo & API Documentation

* **Live API Base URL:** `https://telemedicine-api.onrender.com` 
* **Interactive API Documentation (Swagger UI):** [`https://telemedicine-api.onrender.com/swagger-ui.html`](https://telemedicine-api.onrender.com/swagger-ui.html) 

---

## Core Features

* **Secure Authentication:** JWT-based authentication and registration for Patients and Doctors.
* **Role-Based Access Control:** Differentiated permissions for Patients (`ROLE_PATIENT`) and Doctors (`ROLE_DOCTOR`).
* **Profile Management:** Endpoints for users to create and manage their personal and professional profiles.
* **Appointment Scheduling:** Full CRUD functionality for booking, viewing, and canceling appointments.
* **Containerized Deployment:** Fully containerized with Docker for consistent and portable deployments.
* **CI/CD Pipeline:** Automated builds and testing using GitHub Actions.

---

## Tech Stack

### Backend
* **Java 17**
* **Spring Boot 3**
* **Spring Security (JWT)**
* **Spring Data JPA / Hibernate**
* **PostgreSQL**

### DevOps & Testing
* **Docker & Docker Compose**
* **GitHub Actions (CI/CD)**
* **JUnit 5 & Mockito** (Unit & Integration Testing)
* **Maven** (Build Tool)
* **OpenAPI 3 / Swagger** (API Documentation)

---

## Local Setup & Installation

To run this project locally, you need to have Docker and Docker Compose installed.

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/Vishal-Palve/Telemedicine-Platform.git](https://github.com/Vishal-Palve/Telemedicine-Platform.git)
    cd Telemedicine-Platform
    ```

2.  **Run with Docker Compose:**
    This single command will build the application image and start both the application and database containers.
    ```bash
    docker-compose up --build
    ```

3.  **Access the application:**
    * API will be available at `http://localhost:8080`
    * Swagger UI will be at `http://localhost:8080/swagger-ui.html`

---

## Contact

Vishal Palve - [your.email@example.com](mailto:vishalxpalve@gmail.com) - [LinkedIn Profile URL](https://www.linkedin.com/in/vishalxpalve/)

Project Link: [https://github.com/Vishal-Palve/Telemedicine-Platform](https://github
