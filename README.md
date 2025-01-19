# Project Gallxiv

[English](README.md) | [中文](README_zh.md)

## Overview

Gallxiv is a full-stack web application developed with Spring Boot (backend) and Vue 3 (frontend). It is designed to handle functionalities such as user authentication, post management, and media handling. The project is modular and containerized for ease of deployment.

---

## Features

### Backend Features

- **User Authentication**: Secure login with JWT-based authentication.
- **Post Management**: APIs for creating, retrieving, and managing posts.
- **Media Handling**: APIs for uploading and retrieving images.
- **Spring Boot Configuration**: Flexible configuration using `application.yml`.

### Frontend Features

- **Vue 3 Framework**: Modern frontend development using Composition API.
- **State Management**: Powered by Pinia for application state.
- **API Integration**: Auto-generated API hooks for seamless interaction with the backend.
- **Responsive Design**: Mobile-friendly layouts.

---

## Project Structure

### Backend

```
src/main/kotlin/com/benjk/gallexiv
├── config          # Security and application configuration
├── controller      # REST controllers for APIs
├── data
│   ├── dto         # Data Transfer Objects
│   ├── entity      # Database entities
├── repository      # Database repositories
├── service         # Business logic services
```

### Frontend

```
frontend
├── public          # Static assets (e.g., favicon)
├── src
│   ├── api         # API hooks and schemas
│   ├── components  # Reusable Vue components
│   ├── plugin      # Plugins for Toastify and Vuetify
│   ├── router      # Vue Router configuration
│   ├── stores      # Pinia stores for state management
│   ├── views       # Application views
```

### Key Files

- **Backend**
  - `application.yml`: Main configuration file for the Spring Boot application.
  - `build.gradle.kts`: Gradle build script.
- **Frontend**
  - `vite.config.ts`: Vite configuration.
  - `package.json`: Node.js dependencies and scripts.

---

## Setup

### Prerequisites

- **Java 17**
- **Node.js 16+**
- **Docker**

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd gallxiv-main
   ```
2. Build the project using Gradle:
   ```bash
   ./gradlew build
   ```
3. Run the application:
   ```bash
   java -jar build/libs/gallexiv.jar
   ```

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```

### Docker Setup

1. Start the database container:
   ```bash
   docker-compose -f docker-compose-database.yml up
   ```

---
