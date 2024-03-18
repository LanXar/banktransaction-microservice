# Bank Transaction Microservice

## Project Description

This project is a Spring Boot-based microservice designed to facilitate financial transactions between two bank accounts. It exposes a RESTful API that allows users to perform transactions under various conditions, adhering to specified acceptance criteria, such as successful transactions, handling insufficient balances, preventing transfers between the same account, and validating account existence.

### Features

- **Transaction Processing:** Allows transferring amounts between two different accounts if both exist and the source account has sufficient funds.
- **Balance Validation:** Ensures transactions are only processed if the source account has enough balance.
- **Error Handling:** Provides meaningful error messages for insufficient funds, account identity mismatches, and non-existent accounts.
- **In-Memory Database:** Utilizes an H2 in-memory database for demonstration and testing purposes.

## Getting Started

### Prerequisites

- JDK 17: Ensure you have at least this JDK version installed.
- Maven latest version 3.9.6: Required for project dependency management and build.
- Visual Studio Code (VS Code) used with the following extensions:
  - Extension Pack for Java
  - Spring Boot Extension Pack
  - Spring Initializr Java Support
  - Lombok Annotations Support for VS Code

### Setup Instructions

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/LanXar/banktransaction-microservice.git
   ```
   ```bash
   cd bank-transaction-microservice
   ```

2. **Project Initialization:**

   This project is set up with Spring Initializr and Maven. Dependencies include Spring Web, Spring Data JPA, H2 Database, and Lombok.

3. **Build the Project:**

   ```bash
   mvn clean install
   ```

4. **Run the Application:**

   ```bash
   mvn spring-boot:run
   ```

   The server will start, and the microservice will be accessible on `http://localhost:8080`.

## Accessing the H2 Console

The H2 console is enabled by default for this project, allowing direct access to the in-memory database via a web interface for debugging and testing:

- While the application is running, navigate to http://localhost:8080/h2-console.
- Log in with the JDBC URL jdbc:h2:mem:testdb, using username haris and password password.
- Now, you can view and interact with the database directly through the H2 console.


## Running Tests

To run the project's automated tests, use the following command:

  ```bash
  mvn test
  ```

### API Usage

- **Create Transaction:**

- **Endpoint:** `POST /transactions`
- **Description:** Transfers a specified amount from one account to another.
- **Request Body:**

  ```json
  {
    "sourceAccountId": "1",
    "targetAccountId": "2",
    "amount": 100.00,
    "currency": "USD"
  }
  ```

- sourceAccountId: ID of the account to debit.

- targetAccountId: ID of the account to credit.

- amount: The amount to be transferred.

- currency: The currency of the transaction (USD, EUR).

**Success Response:**

```json
{
  "message": "Transaction successful",
  "transactionId": "1",
  "date": "2024-03-18T14:00:00Z"
}
```
**Error Response:**

```json
{
  "message": "Insufficient balance in source account.",
  "transactionId": "1",
  "date": "2024-03-18T14:20:00Z"
}
```

- **Get Account Details:**

  `GET /accounts/{accountId}`

- Description: Retrieves details of the specified account, including the current balance.

- URL Parameters:

  - accountId: The ID of the account whose details are being requested.

**Success Response:**

```json
{
  "accountId": "1",
  "balance": 900.00,
  "currency": "USD",
  "createdAt": "2024-03-18T14:30:00Z"
}
```