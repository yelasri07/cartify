# Cartify: Microservices E-commerce Platform

Cartify is a robust, end-to-end e-commerce marketplace built using a microservices architecture. It leverages **Spring Boot** for the backend, **Angular** for the frontend, and **MongoDB** for persistent storage, with **Kafka** for asynchronous communication — all backed by an automated **Jenkins CI/CD pipeline** with **SonarQube**-enforced quality gates.

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

### CI/CD Pipeline Flow
```text
Developer Push / Pull Request
            |
            v
      GitHub Webhook
            |
            v
  Jenkins Multibranch Pipeline
    (build → test → scan)
            |
            v
   SonarQube Analysis (per service)
     product-service, user-service
            |
            v
      Quality Gate Check
      (waitForQualityGate)
       /              \
   PASS              FAIL
     |                  |
     v                  v
 Deploy /           Pipeline aborts
 Merge allowed      Merge blocked
  (status → GitHub)  (status → GitHub)
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

### ⚙️ CI/CD & Code Quality
- **Jenkins Multibranch Pipeline:** Auto-discovers branches and pull requests directly from GitHub, triggered by webhook on every push and PR event.
- **Automated Build & Test:** Each Spring Boot service is built and tested independently via Maven Wrapper before deployment.
- **SonarQube Static Analysis:** Every service (`product-service`, `user-service`, …) is scanned as its own SonarQube project, run against a merged preview of the PR and the target branch.
- **Quality Gates:** The pipeline automatically fails if a service doesn't meet its quality gate (coverage, bugs, vulnerabilities, code smells, duplication).
- **Branch Protection:** Merging into `main` requires both a passing Jenkins/SonarQube status check **and** an approved code review — enforced directly by GitHub branch protection rules.
- **Rollback Strategy:** Failed deployments automatically roll back to the last known-good commit (`git checkout ${GIT_PREVIOUS_SUCCESSFUL_COMMIT}`).
- **Notifications:** Email alerts on build success/failure.

### 🛠️ Technical Stack
- **Backend:** Java 17+, Spring Boot 3, Spring Cloud, Spring Security.
- **Frontend:** Angular 19, Tailwind CSS, Angular Material.
- **Data:** MongoDB (per-service instances).
- **Messaging:** Apache Kafka.
- **Infrastructure:** Docker & Docker Compose (rootless).
- **CI/CD:** Jenkins (Multibranch Pipeline, custom `jdk21` + Temurin 17 agent image), ngrok (webhook tunnel).
- **Code Quality:** SonarQube Community Edition, Docker Compose (Postgres-backed).

---

## 🛠️ Setup & Installation

### Prerequisites
- Docker & Docker Compose
- Java 17 (for local builds)
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

### 2. SSL Certificate Setup
To support end-to-end HTTPS, you must provide SSL certificates in the `frontend/` directory. Create or place the following files in `frontend/`:

- `secureCertificate.crt`: Your SSL certificate.
- `private.key`: The private key for your certificate.
- `securePassphrase`: A file containing the passphrase for the private key (if applicable).

Example command to generate self-signed certificates for local development:
```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout frontend/private.key \
  -out frontend/secureCertificate.crt
# Create a dummy passphrase file if your key is not encrypted
touch frontend/securePassphrase
```

### 3. Build the Services
Use the provided build script to package all backend microservices:
```bash
chmod +x build.sh
./build.sh
```

### 4. Launch the Application
Run the entire system using Docker Compose:
```bash
docker compose up -d
```
*Note: The services will wait for the Discovery Service to be healthy before starting.*

The application will be available at `https://localhost:4200`.

---

## 🔁 CI/CD Pipeline

### Jenkins Setup
- Jenkins runs with a custom agent image (`sphinex/buy01-agent`, based on `jenkins/agent:jdk21` with Temurin JDK 17 installed).
- Exposed externally via ngrok to receive GitHub webhooks.
- Configured as a **Multibranch Pipeline** using the GitHub Branch Source plugin:
  - **Discover branches:** *Only branches that are also filed as PRs*
  - **Discover pull requests from origin:** *Merging the pull request with the current target branch revision*
  - GitHub credentials attached (authenticated, not anonymous) so Jenkins can post commit statuses back to PRs.

### Pipeline Stages
1. **Checkout SCM** — fetches the PR branch merged against the current `main`.
2. **SonarQube Analysis** — runs `mvn clean verify sonar:sonar` per service, scoped via `dir(<service>)`, using `withSonarQubeEnv` to register the analysis with Jenkins.
3. **Quality Gate** — `waitForQualityGate abortPipeline: true`, triggered by a SonarQube webhook (`/sonarqube-webhook/`) reporting back to Jenkins.
4. **Deploy** (on `main` only) — Docker-based deploy with health checks and automatic rollback on failure.
5. **Notify** — email notification of the final build/deploy status.

### Branch Protection
The `main` branch requires, before merge:
- ✅ At least one approved pull request review
- ✅ A passing Jenkins CI status check (build, tests, and SonarQube quality gate)
- ✅ Branch up to date with `main` before merging

### Code Review & Approval Process
- All changes must go through a pull request — direct pushes to `main` are disabled.
- SonarQube-flagged issues must be resolved, or explicitly justified with a "Won't Fix" comment, before a reviewer approves.
- The Jenkins/SonarQube status check and reviewer approval are both required; neither alone is sufficient to merge.

### SonarQube Setup
- Runs via Docker Compose with a Postgres backing database (not the bundled embedded DB).
- One SonarQube project per microservice (`product-service`, `user-service`, …), each with its own project key and access token, scoped to that service's subdirectory in the monorepo.
- Tokens are stored in the Jenkins Credentials Store — never hardcoded in the Jenkinsfile or scripts.

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

---

## 🧪 Evaluation Criteria Compliance

### MR-Jenk (CI/CD)
- ✅ **Automation:** GitHub-triggered Jenkins Multibranch Pipeline (push + PR events).
- ✅ **Testing Integration:** Backend tests run via Maven; pipeline fails on test failure.
- ✅ **Deployment:** Docker-based deploy with health checks and rollback.
- ✅ **Notifications:** Email alerts on build/deploy status.
- ✅ **Pipeline Quality:** Declarative pipeline, per-service staging, Credentials Store for secrets.

### SafeZone (Code Quality & Security)
- ✅ **Setup & Configuration:** SonarQube via Docker Compose, Postgres-backed.
- ✅ **Integration:** GitHub → Jenkins → SonarQube → Jenkins → GitHub status loop.
- ✅ **Code Analysis:** Per-service static analysis and vulnerability detection.
- ✅ **Automation:** Scans triggered automatically on every push and pull request.
- ✅ **Process:** Mandatory PR review + passing quality gate required before merge.

### General
- ✅ **Microservices:** Separated into User, Product, Media, Gateway, and Discovery.
- ✅ **Persistence:** Independent MongoDB instances for each service.
- ✅ **Media Security:** MIME validation and 2MB limit enforced in Media Service.
- ✅ **Frontend:** Responsive Angular UI with Guards and Interceptors.
- ✅ **Observability:** Actuator `/health` endpoints configured.

