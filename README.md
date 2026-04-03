# Smart Canteen Management System (Backend)

A robust, scalable RESTful API built with Java and Spring Boot to manage college canteen operations. 
This system handles everything from real-time order queuing and dynamic wait-time estimation to secure role-based access control.

## 🚀 Key Features

* **Smart Queue & Wait Time Estimation:** Calculates estimated preparation times dynamically based on current queue size and active staff count.
* **Real-Time Notifications:** Uses WebSockets (STOMP) to push live order status updates directly to users.
* **Role-Based Access Control:** Distinct workflows for `ADMIN`, `STAFF`, and `STUDENT` roles.
* **Secure Authentication:** Implements stateless JWT authentication with secure refresh token rotation.
* **Media Integration:** Seamless menu item image uploads integrated directly with Cloudinary.

## 🛠️ Tech Stack

* **Core:** Java 17, Spring Boot 3.x
* **Database:** MySQL, Spring Data JPA, Hibernate
* **Security:** Spring Security, JWT (JSON Web Tokens)
* **Messaging:** Spring WebSocket, STOMP
* **Storage:** Cloudinary API

## 🏗️ Architecture Highlights

* **Event-Driven Notifications:** Utilizes Spring's `@TransactionalEventListener` to ensure WebSocket notifications are only dispatched upon successful database commits.
* **Separation of Concerns:** Clean architecture utilizing Controllers, Services, Repositories, and DTOs (mapped via ModelMapper).
* **Custom Exception Handling:** Global `@RestControllerAdvice` for consistent API error responses.

## ⚙️ Local Setup

1. **Clone the repository:**
   `git clone https://github.com/razzak118/apsit_canteen_management.git`
2. **Configure Database:**
   Update `src/main/resources/application.properties` with your MySQL credentials:
   ```properties
   DB_URL=jdbc:mysql://localhost:3306/apsit_canteen_management
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
