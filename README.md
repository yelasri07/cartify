# Buy-01: Microservices E-commerce Platform

Buy-01 is a robust, end-to-end e-commerce marketplace built using a microservices architecture. It leverages **Spring Boot** for the backend, **Angular** for the frontend, and **MongoDB** for persistent storage, with **Kafka** for asynchronous communication.

## 🏗️ Architecture Overview

The system is decomposed into several specialized services, each handling a specific domain of the application:

- **Discovery Service (Eureka):** Service registry for dynamic service discovery.
- **API Gateway:** Central entry point for all client requests, handling routing and CORS.
- **User Service:** Manages authentication, user profiles, and roles (CLIENT, SELLER).
- **Product Service:** Handles product catalog management and CRUD operations.
- **Media Service:** Manages image uploads and storage (integrated with Cloudinary).
- **Frontend:** A modern Angular SPA for user interaction.

### System Diagram (Simplified)
```text
[Frontend (Angular)] -> [API Gateway (8080)]
                            |
           ---------------------------------------
           |                |                    |
    [User Service]   [Product Service]    [Media Service]
           |                |                    |
      [User DB]        [Product DB]         [Media DB]
           |________________|____________________|
                            |
                    [Kafka (Broker)]
```

---

## 🚀 Key Features

### 🔐 Security & Auth
- **JWT-based Authentication:** Secure access to protected resources.
- **Role-Based Access Control (RBAC):** Distinct permissions for `CLIENT` and `SELLER`.
- **Password Security:** Hashing and salting with BCrypt.
- **Ownership Enforcement:** Sellers can only manage their own products and media.

### 📦 Product Management
- Sellers can Create, Read, Update, and Delete (CRUD) products.
- Clients can browse the public product catalog.
- Products are linked to media URLs managed by the Media Service.

### 🖼️ Media Handling
- Secure file uploads with MIME type validation (`image/*`).
- Strict file size limits (max 2MB per upload).
- Cloudinary integration for scalable image hosting.

### 🛠️ Technical Stack
- **Backend:** Java 17+, Spring Boot 3, Spring Cloud, Spring Security.
- **Frontend:** Angular 19, Tailwind CSS, Angular Material.
- **Data:** MongoDB (per-service instances).
- **Messaging:** Apache Kafka.
- **Infrastructure:** Docker & Docker Compose.

---

## 🛠️ Setup & Installation

### Prerequisites
- Docker & Docker Compose
- Java 17+ (for local builds)
- Node.js & npm (for frontend)
- Angular CLI (`npm install -g @angular/cli`)

### 1. Environment Configuration
Create a `.env` file in the root directory based on `.env.example`:
```env
DB_USERNAME=buy01
DB_PASSWORD=buy01
DB_NAME=buy01
CLOUDINARY_URL=your_cloudinary_url_here
```

### 2. Build the Services
Use the provided build script to package all backend microservices:
```bash
chmod +x build.sh
./build.sh
```

### 3. Launch the Infrastructure
Run the entire system using Docker Compose:
```bash
docker-compose up -d
```
*Note: The services will wait for the Discovery Service to be healthy before starting.*

### 4. Start the Frontend
Navigate to the frontend directory and start the development server:
```bash
cd frontend
npm install
npm start
```
The application will be available at `http://localhost:4200`.

---

## 📖 API Documentation

The **API Gateway** (Port 8080) routes traffic to the following endpoints:

| Service | Endpoint | Description | Auth Required |
|---------|----------|-------------|---------------|
| **User** | `POST /auth/register` | User registration | No |
| **User** | `POST /auth/login` | User login -> JWT | No |
| **User** | `GET /users/me` | Current user profile | Yes |
| **Product** | `GET /api/products` | List all products | No |
| **Product** | `POST /api/products` | Create product | Yes (SELLER) |
| **Media** | `POST /media` | Upload image | Yes (SELLER) |

---

## 🛠️ Developer Scripts

- `build.sh`: Packages all Spring Boot services using Maven Wrapper.
- `docker_clean.sh`: Forcefully stops and removes all containers, images, and volumes.
- `run_docker_dns.sh`: Helper to restart Docker with custom DNS settings (if needed for registry access).
- `setup.sh`: Quick build and deploy script.

---

## 🧪 Evaluation Criteria Compliance

- ✅ **Microservices:** Separated into User, Product, Media, Gateway, and Discovery.
- ✅ **Persistence:** Independent MongoDB instances for each service.
- ✅ **Media Security:** MIME validation and 2MB limit enforced in Media Service.
- ✅ **Frontend:** Responsive Angular UI with Guards and Interceptors.
- ✅ **Observability:** Actuator `/health` endpoints configured.
