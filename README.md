# 💍 Enterprise Matrimony & Matchmaking Backend API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%2F21-orange.svg)](https://www.oracle.com/java/)
[![Spring Security](https://img.shields.io/badge/Security-JWT%20%2B%20BCrypt-blue.svg)](https://spring.io/projects/spring-security)
[![WebSocket](https://img.shields.io/badge/WebSocket-STOMP%20%2F%20SockJS-purple.svg)](https://spring.io/guides/gs/messaging-stomp-websocket/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A robust, enterprise-grade, highly scalable backend platform built with **Spring Boot** for a modern matchmaking and matrimony web application. This service features advanced partner matching algorithms, dynamic multi-criteria search with pagination, a secure interest workflow, real-time bidirectional WebSocket messaging, and stateless JWT authentication.

---

## 🏗️ Architecture & System Design

The application follows a clean **N-Tier Layered Architecture** (Controller → Service → Repository → Database) combined with clean separation of concerns, DTO mapping (`ModelMapper`), and robust global exception handling.

```
┌────────────────────────────────────────────────────────┐
│                      Client Layer                      │
│            (Web / Mobile / Postman / Swagger)          │
└───────────┬────────────────────────────────┬───────────┘
            │ REST HTTP (JSON)               │ WebSocket (STOMP/SockJS)
            ▼                                ▼
┌────────────────────────────────────────────────────────┐
│                   Spring Security                      │
│            (Stateless JWT Filter & AuthManager)        │
└───────────┬────────────────────────────────┬───────────┘
            │                                │
            ▼                                ▼
┌──────────────────────┐         ┌──────────────────────┐
│  REST Controllers    │         │  WebSocket Gateway   │
│  (/auth, /profiles,  │         │  (/app & /topic)     │
│   /matches, /chat)   │         │                      │
└───────────┬──────────┘         └──────────┬───────────┘
            │                               │
            └───────────────┬───────────────┘
                            ▼
┌────────────────────────────────────────────────────────┐
│                     Service Layer                      │
│      (Business Rules, Scoring Algorithms, Validators)  │
└───────────────────────────┬────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────┐
│                   Persistence Layer                    │
│      (Spring Data JPA, Hibernate, Criteria Queries)    │
└───────────────────────────┬────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────┐
│                     Database (SQL)                     │
│                      (PostgreSQL)          │
└────────────────────────────────────────────────────────┘
```

---

## 🚀 Core Features & Capabilities

1. **🔒 Enterprise Authentication & Security**
    - Stateless JWT (JSON Web Token) authentication using `jjwt` (0.12.5).
    - Secure password hashing via `BCryptPasswordEncoder`.
    - Role-Based Access Control (`ROLE_USER`, `ROLE_ADMIN`).
    - Protected endpoints with custom `OncePerRequestFilter`.

2. **⚙️ Intelligent Matchmaking Algorithm**
    - Configurable partner preferences (Age range, Height range, Preferred City, Education, Marital Status).
    - Weighted compatibility scoring engine ranking potential candidates dynamically.
    - Paginated results using Spring Data `Pageable`.

3. **🔍 Dynamic Criteria Search**
    - Multi-parameter filtering on profiles with sorting and pagination (`Page<ProfileDto>`).
    - Soft-delete safe architecture (`deleted_at` tracking).

4. **💌 Interest Workflow Engine**
    - Send, accept, or reject match interests.
    - Strict business validation guards preventing redundant requests.

5. **💬 Real-Time Messaging System (WebSockets + STOMP)**
    - Bidirectional real-time communication using Spring WebSocket broker.
    - **Strict Connection Guard:** Users can only exchange chat messages if an **`ACCEPTED`** interest connection exists between them.
    - Persistent message storage (`chat_messages` table) enabling offline chat history retrieval via REST.

6. **📊 Pagination, Sorting & Performance**
    - Zero memory bloat: Large datasets are sliced at the database level using `PageRequest`.

---

## 📁 Project Structure

```filesystem
src/
├── main/
│   ├── java/com/eu/demomatrimony/
│   │   ├── config/          # WebSocketConfig, SecurityConfig, ModelMapperConfig
│   │   ├── controllers/     # AuthController, ProfileController, MatchController, InterestController, ChatController
│   │   ├── dto/             # AuthResponse, LoginRequest, RegisterRequest, ProfileDto, PartnerPreferenceDto, ChatMessageDto, etc.
│   │   ├── models/          # BaseModel, User, Profile, PartnerPreference, Interest, ChatMessage, Role, InterestStatus
│   │   ├── repositories/    # UserRepository, ProfileRepository, PreferenceRepository, InterestRepository, ChatMessageRepository
│   │   ├── security/        # JwtTokenProvider, JwtAuthenticationFilter, CustomUserDetailsService
│   │   ├── service/         # AuthService, ProfileService, MatchService, InterestService, ChatService
│   │   └── serviceImpl/     # Concrete implementations of business logic
│   └── resources/
│       └── application.yml  # Database, JWT, and Server configurations
└── test/
    └── java/com/eu/demomatrimony/
        └── serviceImpl/     # Unit and integration test suites
```

---

## 🗄️ Database Entity-Relationship Overview

- **`users`**: Core authentication record (`id`, `email`, `password`, `roles`).
- **`profiles`**: Extended user biographical and demographic details (`id`, `user_id`, `name`, `age`, `gender`, `city`, `education`, `marital_status`, `height`).
- **`partner_preferences`**: Matchmaking criteria linked 1-to-1 with a profile.
- **`interests`**: Tracks sender, receiver, and `InterestStatus` (`PENDING`, `ACCEPTED`, `REJECTED`).
- **`chat_messages`**: Real-time message logs (`id`, `sender_id`, `receiver_id`, `content`, `timestamp`).

---

## 🔌 API Endpoints Reference

### 🔐 Authentication (`/auth`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/register` | Register a new user account + profile | Public |
| `POST` | `/auth/login` | Authenticate credentials & return JWT | Public |

### 👤 Profiles & Search (`/profiles`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/profiles/search` | Dynamic criteria search with `page`, `size`, `sort` | Authenticated |

### 💖 Matching (`/matches`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/matches/{profileId}` | Get score-ranked matches with pagination | Authenticated |

### 💌 Interests (`/interests`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/interests/send` | Send connection interest to a profile | Authenticated |
| `POST` | `/interests/respond` | Accept or reject a received interest | Authenticated |

### 💬 Real-Time Messaging & History
| Method | Endpoint / Destination | Description | Type |
| :--- | :--- | :--- | :--- |
| `GET` | `/messages/{user1Id}/{user2Id}` | Fetch historical chat messages between two users | REST API |
| `WS` | `/ws` | STOMP SockJS handshake endpoint | WebSocket |
| `SEND` | `/app/chat.sendMessage` | Send chat message payload (validated against accepted status) | STOMP Message |
| `SUB` | `/topic/messages/{receiverId}` | Listen for incoming real-time messages | STOMP Topic |

---

## ⚙️ Getting Started & Local Setup

### Prerequisites
- **Java JDK** 17 or 21 installed.
- **Maven** 3.8+ or your preferred IDE (IntelliJ IDEA, Eclipse).
- **PostgreSQL / MySQL** (Optional: defaults to H2 in-memory database for rapid local testing if configured).

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/demomatrimony.git
cd demomatrimony
```

### 2. Configure `application.properties`

Create or update `src/main/resources/application.properties`:

```properties
# Application Info
spring.application.name=demomatrimony

# Server Configuration
server.port=8080

# PostgreSQL Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/DemoMatrimony
spring.datasource.username=postgres
spring.datasource.password=maruf

# JPA & Hibernate Settings
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Security Configurations
app.jwt-secret=9a2f8c2e4b1a6d8e0f3c5b7a9e1d3f5a7c9b1e3f5a7c9b1e3f5a7c9b1e3f5a7c
app.jwt-expiration-milliseconds=86400000

### 3. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on port `8080`.

---

## 🧪 Running Tests

Execute the automated unit and integration test suites:
```bash
mvn test
```

---

## 📄 License

Distributed under the **MIT License**. See `LICENSE` for more information.