# 🧩 Product Service Implementation (Spring Boot Exercise)

## 📘 Overview

In this exercise, you will build a **Spring Boot microservice** that manages product data.  
Your goal is to design and implement a RESTful service from scratch, following clean architecture principles and using industry-standard tools.

You will work on a dedicated branch named:  
`productService_exercise_1`

---

## 🛠️ Technical Requirements

**General Setup**
- Spring Boot version: **3.5.6**
- Java version: **21**
- Build tool: **Maven**
- Database: **PostgreSQL**

You are expected to set up the project structure, dependencies, and configurations on your own —  
and every week to add something new to your app 😉.

---

## 📦 Functional Requirements

Your service should provide functionality to:

1. **Store a single product**
2. **Store multiple products at once**
3. **Return all stored products**
4. **Return products manufactured between the years 2023 and 2025**
5. **Delete a product by its ID**

---

## 🌐 API Design

Design a simple and intuitive REST API that allows clients to perform the actions described above.  
Decide on appropriate endpoints, HTTP methods, and request/response structures.

---

## 🧱 Architecture

Use a **layered architecture** to structure your application logically.  
You should separate responsibilities (e.g., web, service, and persistence layers) and design clear interfaces between them.

---

## 💾 Database Integration

Integrate the application with a **PostgreSQL** database.  
Define how product data will be stored and retrieved.

You will need to research how to:
- Configure database access
- Create and map entities
- Persist and query data

---

## 🧪 Testing (Optional)

Add unit or integration tests if you want to validate your solution and improve reliability.

---

## 📋 Task Management

You can organize the work into small subtasks (e.g., API design, database setup, service layer implementation, testing, etc.)  
and track them in your **plan** or personal task list.

---

## 📤 Submission Guidelines

1. Push your implementation to the branch:  
   **`productService_exercise_1`**

2. Once finished, open a **Pull Request (PR)** to the **`development`** branch.

3. Add a short description in the PR summarizing what you implemented.

---

## ⚠️ Notes

- Do **not** copy or paste solutions from chatgpt if you really want to learn something.
- Research the required steps and concepts independently.
- Always **test what you code**.
- Focus on:
    - Clean and readable code
    - Logical structure and naming
    - Good use of Spring Boot conventions  
    - always test what 
