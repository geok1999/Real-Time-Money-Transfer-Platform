# 💰 Money Transfer Banking API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A secure, scalable, and production-ready RESTful API for financial money transfer operations. Built with enterprise-grade architecture patterns and designed to handle high-volume banking transactions with robust security and compliance features.

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     API Gateway Layer                       │
├─────────────────────────────────────────────────────────────┤
│  Account Management  │  Transaction Processing  │  Security │
├─────────────────────────────────────────────────────────────┤
│                   Business Logic Layer                      │
├─────────────────────────────────────────────────────────────┤
│                     Data Access Layer                       │
├─────────────────────────────────────────────────────────────┤
│                    PostgreSQL Database                      │
└─────────────────────────────────────────────────────────────┘
```

## ✨ Key Features

### 💸 Core Banking Operations
- **Secure Money Transfers** - Real-time balance updates with ACID compliance
- **Account Management** - Create, update, and manage bank accounts
- **Multi-Currency Support** - Handle transfers in USD, EUR, GBP, and more
- **Transaction History** - Complete audit trail of all financial operations

### 🔒 Security & Compliance
- **Concurrent Transaction Handling** - Optimistic locking prevents race conditions
- **Input Validation** - Comprehensive validation with detailed error responses
- **Audit Logging** - Complete transaction audit trail for compliance
- **Error Handling** - Graceful error handling with meaningful responses

### 🏛️ Enterprise Architecture
- **RESTful Design** - Clean, intuitive API endpoints
- **Spring Boot Framework** - Modern Java enterprise framework
- **JPA/Hibernate** - Robust database abstraction layer
- **Retry Mechanisms** - Automatic retry for transient failures

## 🚀 Quick Start

### Prerequisites

- **Java 17** or higher
- **Maven 3.9+**
- **PostgreSQL 12+**
- **Docker** (optional, for containerized database)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/money-transfer-api.git
   cd money-transfer-api
   ```

2. **Start PostgreSQL Database**
   ```bash
   # Using Docker Compose (Recommended)
   docker-compose up -d
   
   # Or use your local PostgreSQL instance
   # Update connection details in application.properties
   ```

3. **Configure Application**
   ```bash
   # Copy and modify configuration
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   
   # Update database credentials
   spring.datasource.url=jdbc:postgresql://localhost:5433/postgres
   spring.datasource.username=postgres
   spring.datasource.password=1234
   ```

4. **Build and Run**
   ```bash
   # Build the application
   mvn clean install
   
   # Run the application
   mvn spring-boot:run
   
   # Application will start on http://localhost:8080
   ```

## 📡 API Endpoints
### [Swagger link](http://localhost:8080/swagger-ui/index.html)

### Account Management

| Method | Endpoint | Description | Status |
|--------|----------|-------------|---------|
| `POST` | `/api/accounts/account/{id}` | Create or update account | ✅ |
| `GET` | `/api/accounts/account/{id}` | Get account details | ✅ |
| `GET` | `/api/accounts/account` | List all accounts | ✅ |

### Transaction Operations

| Method | Endpoint | Description | Status |
|--------|----------|-------------|---------|
| `POST` | `/api/transactions/transaction` | Execute money transfer | ✅ |
| `GET` | `/api/transactions/LogTransaction` | Get transaction history | ✅ |

### Example Usage

#### Create Account
```bash
curl -X POST http://localhost:8080/api/accounts/account/1 \
  -H "Content-Type: application/json" \
  -d '{
    "balance": 1000.00,
    "currency": "USD",
    "createdAt": "2024-01-15T12:00:00"
  }'
```

#### Execute Money Transfer
```bash
curl -X POST http://localhost:8080/api/transactions/transaction \
  -H "Content-Type: application/json" \
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

## 🔒 Business Rules & Validation

### Transfer Validation
- ✅ **Positive Amount** - Transfer amount must be greater than 0.01
- ✅ **Sufficient Balance** - Source account must have adequate funds
- ✅ **Currency Matching** - All parties must use the same currency
- ✅ **Account Validation** - Both accounts must exist and be active
- ✅ **Self-Transfer Prevention** - Cannot transfer to the same account

### Concurrency Control
- **Optimistic Locking** - Prevents lost updates in concurrent scenarios
- **Automatic Retry** - Handles temporary lock failures gracefully
- **Transaction Isolation** - Ensures ACID compliance for all operations

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=TransactionServiceTest

# Run integration tests
mvn test -Dtest=*IT
```

### Test Coverage
- **Unit Tests** - Service layer business logic
- **Integration Tests** - End-to-end API testing
- **Concurrent Testing** - Multi-threaded transaction scenarios
- **Database Tests** - Repository layer validation

## 🏗️ Architecture Roadmap

### Current Implementation ✅
- RESTful API with Spring Boot
- PostgreSQL database integration
- Concurrent transaction handling
- Comprehensive input validation
- Docker containerization

### Planned Enhancements 🚧
- **JWT Authentication** - Secure user authentication and authorization
- **Multi-Module Architecture** - Modular design for scalability
- **Kafka Integration** - Event-driven architecture for real-time processing
- **Audit Logging** - Complete transaction audit trail
- **API Rate Limiting** - Request throttling for security
- **Monitoring & Metrics** - Prometheus/Grafana integration

## 🐳 Docker Support

### Development Environment
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Production Deployment
```dockerfile
# Build production image
docker build -t money-transfer-api:latest .

# Run production container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  money-transfer-api:latest
```

## 📊 Performance Considerations

### Database Optimization
- **Connection Pooling** - HikariCP for optimal database connections
- **Query Optimization** - JPA queries optimized for performance
- **Index Strategy** - Strategic indexing on frequently queried fields

### Concurrency Handling
- **Optimistic Locking** - High concurrency with minimal blocking
- **Retry Mechanisms** - Graceful handling of temporary conflicts
- **Transaction Isolation** - READ_COMMITTED for optimal performance

## 🛡️ Security Features

### Current Security
- **Input Validation** - Comprehensive request validation
- **Error Handling** - Secure error responses without information leakage
- **SQL Injection Prevention** - JPA/Hibernate parameterized queries

### Planned Security Enhancements
- **JWT Authentication** - Stateless authentication tokens
- **Role-Based Access Control** - Fine-grained permissions
- **Rate Limiting** - API request throttling
- **Audit Logging** - Complete security event tracking

## 🤝 Contributing

### Development Workflow
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Standards
- Follow Google Java Style Guide
- Maintain test coverage above 80%
- Include comprehensive documentation
- Add integration tests for new endpoints

## 📋 Environment Configuration

### Development
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5433/postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logging
logging.level.root=INFO
logging.level.com.bankapi=DEBUG
```

### Production
```properties
# Database Configuration
spring.datasource.url=${DATABASE_URL}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Security
server.ssl.enabled=true
management.endpoints.web.exposure.include=health,info
```

## 📈 Monitoring & Health Checks

### Health Endpoints
- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics

### Database Health
```bash
# Check database connectivity
curl http://localhost:8080/actuator/health/db
```