# RESTful API for Money Transfer

This project implements a simple RESTful API using Spring Boot (Java) for handling financial transactions. The main functionality is to transfer money between two bank accounts, adhering to the specified acceptance criteria.

## Table of Contents

- [Features](#features)
- [Endpoints](#endpoints)
- [Data Models](#data-models)
- [General Notes](#general-notes)
- [Basic Instructions run the Application](#basic-instructions-run-the-application)

## Features

- Happy path for money transfer between two accounts.
- Handling insufficient balance to process money transfer.
- Preventing transfer between the same account.
- Validating the existence of accounts during the transaction.
- Simple and extensible data models for accounts and transactions.

## Endpoints

### 1. Money Transfer

- **Endpoint**: `POST /api/feel/free/to/decide/the/uri/format`
- **Request Body**:
  ```json
  {
    "sourceAccountId": "string",
    "targetAccountId": "string",
    "amount": 10.5,
    "currency": "GBP"
  }
  ```
- **Response**:
    - Success:
      ```json
      {
        "feelFree": "to decide the response type"
      }
      ```
    - Failure:
      ```json
      {
        "feelFree": "to decide the response type"
      }
      ```

## Data Models

### 1. Account

```json
{
  "id": "Feel free to decide the id type",
  "balance": "Feel free to decide the balance type",
  "currency": "GBP/EUR/USD/orWhateverYouLike",
  "createdAt": "2024-01-15T12:00:00Z",
  "addOther": "optionalFieldsIfNecessary"
}
```

### 2. Transaction

```json
{
  "id": "Feel free to decide the id type",
  "sourceAccountId": "Feel free to decide the account id type",
  "targetAccountId": "Feel free to decide the account id type",
  "amount": "Feel free to decide the amount type",
  "currency": "GBP",
  "addOther": "optionalFieldsIfNecessary"
}
```

## General Notes

- Implement the service as if it were going into production. Consider and apply industry best practices for deployment, scalability, and maintainability.
- Mock external services if needed.
- Choose between Maven or Gradle for dependency management.
- The API is assumed to be public; no security measures are required for this assignment.

## Basic Instructions run the Application

1. Start docker container for PostgreSQL database:
   ```bash
   docker-compose up
    ```
2. Run the application using the following command:
   ```bash
   mvn spring-boot:run
   ```
3.
    1. Use a REST client (e.g., HTTPie,Postman) first to create accounts:
    ```
    POST http://localhost:8080/api/accounts/account/1 id=1 balance=100 currency="USD" createdAt="2024-11-29T12:00:00Z"
    ```
   2. Then, post a transaction to transfer money between accounts:
    ```
    POST http://localhost:8080/api/transactions/transaction sourceAccountId=1 targetAccountId=2 amount=10 currency="USD"
    ```

