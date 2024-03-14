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

- JDK 17: Ensure you have this JDK version installed.
- Maven: Required for project dependency management and build.
- Visual Studio Code (VS Code) with the following extensions:
  - Java Extension Pack
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

### API Usage

- **Create Transaction:**

  `POST /transactions`

  Body:

  ```json
  {
    "sourceAccountId": "1",
    "targetAccountId": "2",
    "amount": 100.00,
    "currency": "USD"
  }
  ```

- **Get Account Details:**

  `GET /accounts/{accountId}`

## Running Tests

To run the project's automated tests, use the following command:

- For Maven:

  ```bash
  mvn test
  ```
