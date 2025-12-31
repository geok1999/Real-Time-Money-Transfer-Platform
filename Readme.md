# 💰 FetraX - Federated Transaction eXecution API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A secure, scalable, and production-ready RESTful API for financial money transfer operations. Built with enterprise-grade architecture patterns and designed to handle high-volume banking transactions with robust security and compliance features.

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    REST API Layer                           │
│         (Controllers with Interface-Based Design)           │
├─────────────────────────────────────────────────────────────┤
│  Account Controller  │  Transaction Controller  │  Auth     │
├─────────────────────────────────────────────────────────────┤
│              JWT Authentication Filter                      │
├─────────────────────────────────────────────────────────────┤
│                   Service Layer                             │
│    (AccountService, TransactionService, AuthService)        │
├─────────────────────────────────────────────────────────────┤
│                  Repository Layer                           │
│              (Spring Data JPA Repositories)                 │
├─────────────────────────────────────────────────────────────┤
│                    PostgreSQL Database                      │
└─────────────────────────────────────────────────────────────┘
```

## ✨ Features

### 💸 Core Banking Operations
- **Secure Money Transfers** - Real-time balance updates with ACID compliance
- **Account Management** - Create, update, and retrieve bank accounts
- **Multi-Currency Support** - USD, EUR, GBP, TRY
- **Transaction History** - Complete audit trail of all financial operations

### 🔒 Security (Implemented)
- **JWT Authentication** - Stateless token-based authentication
- **BCrypt Password Encoding** - Secure password storage
- **Role-Based Access** - USER and ADMIN roles
- **Protected Endpoints** - All API endpoints require authentication (except auth routes)

### 🏛️ Enterprise Architecture
- **Interface-Based Controllers** - Clean separation of API contracts and implementations
- **Optimistic Locking** - Prevents lost updates in concurrent scenarios
- **Spring Retry** - Automatic retry for transient failures (OptimisticLockException, CannotAcquireLockException)
- **Comprehensive Validation** - Jakarta Bean Validation with detailed error responses
- **OpenAPI/Swagger Documentation** - Interactive API documentation

## 🚀 Quick Start

### Prerequisites

- **Java 17** or higher
- **Maven 3.9+**
- **Docker** (for PostgreSQL)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/fetrax-api.git
   cd fetrax-api
   ```

2. **Start PostgreSQL Database**
   ```bash
   docker-compose up -d
   ```

3. **Set JWT Secret (Optional)**
   ```bash
   # Default secret is provided for development
   # For production, set environment variable:
   export JWT_SECRET=your-base64-encoded-secret-key
   ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   Application starts at `http://localhost:8080`

## 📡 API Endpoints

### Swagger Documentation
**[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### Authentication (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/auth/register` | Register new user |
| `POST` | `/api/v1/auth/authenticate` | Login and get JWT token |

### Account Management (Protected)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/account/{id}` | Create or update account |
| `GET` | `/api/v1/account/{id}` | Get account by ID |
| `GET` | `/api/v1/account` | List all accounts |

### Transaction Operations (Protected)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/transaction` | Execute money transfer |
| `GET` | `/api/v1/LogTransaction` | Get transaction history |

## 🔐 Authentication Flow

### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
  }'
```

### 2. Authenticate
```bash
curl -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
  }'
```
Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Use Token for Protected Endpoints
```bash
curl -X GET http://localhost:8080/api/v1/account \
  -H "Authorization: Bearer <your-token>"
```

## 📋 API Examples

### Create Account
```bash
curl -X POST http://localhost:8080/api/v1/account/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "balance": 1000.00,
    "currency": "USD"
  }'
```

### Execute Money Transfer
```bash
curl -X POST http://localhost:8080/api/v1/transaction \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "sourceAccountId": 1,
    "targetAccountId": 2,
    "amount": 100.00,
    "currency": "USD"
  }'
```

## 🏛️ Data Models

### Account
```json
{
  "id": 1,
  "balance": 1000.00,
  "currency": "USD",
  "createdAt": "2024-01-15T12:00:00Z"
}
```

### Transaction
```json
{
  "id": 1,
  "sourceAccountId": 1,
  "targetAccountId": 2,
  "amount": 100.00,
  "currency": "USD",
  "createdAt": "2024-01-15T12:30:00Z"
}
```

### User
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "role": "USER"
}
```

## 🔒 Business Rules & Validation

### Transfer Validation
| Rule | Description |
|------|-------------|
| ✅ Positive Amount | Amount must be between 0.01 and 999,999.99 |
| ✅ Sufficient Balance | Source account must have adequate funds |
| ✅ Currency Matching | Source, target, and transaction must use same currency |
| ✅ Account Validation | Both accounts must exist |
| ✅ Self-Transfer Prevention | Cannot transfer to the same account |

### Password Requirements
- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one number
- At least one special character (@#$%^&+=!*._-)

## 🧪 Testing

```bash
# Run all tests (uses H2 in-memory database)
mvn test

# Run specific test class
mvn test -Dtest=AccountControllerIT

# Run with verbose output
mvn test -X
```

### Test Coverage
- **Unit Tests** - Service layer with Mockito
- **Integration Tests** - Full API testing with MockMvc
- **Concurrency Tests** - Simultaneous transaction scenarios
- **Security Tests** - Authentication and authorization

## ⚙️ Configuration

### Development (application.properties)
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5433/postgres
spring.datasource.username=postgres
spring.datasource.password=1234

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT (24 hours expiration)
jwt.secret=${JWT_SECRET:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}
jwt.expiration=86400000
```

### Test (application.properties)
```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop

jwt.secret=${JWT_SECRET:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}
jwt.expiration=3600000
```

## 🐳 Docker Support

```bash
# Start PostgreSQL
docker-compose up -d

# View logs
docker-compose logs -f

# Stop
docker-compose down
```

### docker-compose.yml
```yaml
services:
  postgres-db:
    image: postgres:latest
    container_name: postgres-db
    ports:
      - "5433:5432"
    restart: always
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 1234
```

## 📊 Concurrency Handling

The application uses **optimistic locking** with automatic retry:

```java
@Retryable(
    retryFor = {CannotAcquireLockException.class, 
                OptimisticLockingFailureException.class, 
                OptimisticLockException.class},
    backoff = @Backoff(delay = 1000)
)
@Transactional(isolation = Isolation.READ_COMMITTED)
public Transaction createNewTransaction(Transaction transaction) { ... }
```

- `@Version` annotation on entities for optimistic locking
- `READ_COMMITTED` isolation level for performance
- Automatic retry with 1-second backoff on conflicts

## 🗂️ Project Structure

```
src/
├── main/java/bank/money/transfer/
│   ├── controllers/
│   │   ├── AccountController.java          # Interface
│   │   ├── TransactionController.java      # Interface
│   │   └── impl/
│   │       ├── AccountControllerImpl.java
│   │       └── TransactionControllerImpl.java
│   ├── domain/
│   │   ├── dto/
│   │   │   ├── Account.java
│   │   │   └── Transaction.java
│   │   └── entities/
│   │       ├── AccountEntity.java
│   │       └── TransactionEntity.java
│   ├── repositories/
│   │   ├── AccountRepository.java
│   │   └── TransactionRepository.java
│   ├── services/
│   │   ├── AccountService.java             # Interface
│   │   ├── TransactionService.java         # Interface
│   │   └── implementation/
│   │       ├── AccountServiceImplementation.java
│   │       └── TransactionServiceImplementation.java
│   ├── security/
│   │   ├── auth/
│   │   │   ├── AuthenticationController.java
│   │   │   ├── AuthenticationService.java
│   │   │   ├── AuthenticationRequest.java
│   │   │   ├── AuthenticationResponse.java
│   │   │   └── RegisterRequest.java
│   │   ├── config/
│   │   │   ├── ApplicationConfig.java
│   │   │   ├── JwtService.java
│   │   │   ├── JWTAuthenticationFilter.java
│   │   │   ├── SecurityConfiguration.java
│   │   │   └── OpenApiConfig.java
│   │   └── user/
│   │       ├── User.java
│   │       ├── UserRepository.java
│   │       └── Role.java
│   ├── exceptions/
│   │   ├── GlobalExceptionHandler.java
│   │   └── security/AuthenticationExceptionHandler.java
│   └── util/
│       └── Currency.java
└── test/
    └── java/bank/money/transfer/
        ├── controllers/
        │   ├── AccountControllerIT.java
        │   ├── TransactionControllerIT.java
        │   └── security/
        │       ├── TestSecurityIT.java
        │       └── AuthHelperUtils.java
        └── services/implementation/
            ├── AccountServiceImplementationTest.java
            └── TransactionServiceImplementationTest.java
```

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Runtime |
| Spring Boot | 3.4.4 | Framework |
| Spring Security | - | Authentication/Authorization |
| Spring Data JPA | - | Data persistence |
| PostgreSQL | Latest | Production database |
| H2 | - | Test database |
| JWT (jjwt) | 0.11.5 | Token generation/validation |
| Lombok | - | Boilerplate reduction |
| SpringDoc OpenAPI | 2.8.6 | API documentation |
| Hibernate Validator | 8.0.1 | Bean validation |

## 🚧 Roadmap

- [ ] Refresh token implementation
- [ ] Multi-module Maven architecture
- [ ] Currency exchange rates integration
- [ ] Audit logging with Spring AOP
- [ ] API rate limiting
- [ ] Prometheus/Grafana monitoring
- [ ] Kubernetes deployment configs

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
