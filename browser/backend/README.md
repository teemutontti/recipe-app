# Recipe App - Backend

This is the backend for the Recipe App, built with Java Spring Boot. It serves as a shared backend for both Android and browser versions of the app. The backend handles user authentication, role-based access control (RBAC), and data storage. It uses JWT for authentication, encrypts sensitive data at rest, and stores information in a MySQL database.

## Features

* **User Authentication**: Users can sign up, log in, and access protected routes using JWT tokens.
* **Role-Based Access Control (RBAC)**: Supports roles (e.g., ADMIN, USER) with role-based access restrictions on certain routes based on roles.
* **Database Integration**: The backend integrates with a relational database to store users, logs and foods.
* **Security**: Passwords are securely hashed (using Argon2), and sensitive data is encrypted at rest.
* **Dockerized Deployment**: Supports Docker and Docker Compose for easy local setup and Azure deployment.

## Technologies

* **Java 21+**
* **Spring Boot 3.3.4**
* **Spring Security**: For securing endpoints and authentication management.
* **JWT (JSON Web Tokens)**: For authentication and token-based session management.
* **JPA/Hibernate**: For Object-Relational Mapping (ORM) and managing database.
* **MySQL**: For relational database management.
* **Gradle**: Build tools for managing dependencies and running the application.
* **Docker/Docker Compose**: For containerized deployment and local setup.

## Installation & Setup

### Prequisites

* **Java 21+**
* **Docker & Docker Compose**
* **MySQL** (if running locally)

### Clone the Repository

```
git clone https://github.com/teemutontti/recipe-app.git
cd recipe-app/browser/backend
```

### Generate RSA Keys & AES Key

Before running the application, you must generate an **RSA key pair** for JWT signing and an **AES key** for encryption.

#### 🔑 Generate RSA Private Key

```
openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048
```

#### 🔑 Generate RSA Public Key

```
openssl rsa -in private.pem -pubout -out public.pem
```

#### 🔑 Generate AES Secret Key

```
openssl rand -base64 32 > aes.key
```

Once the keys are generated, use the generated values (ignore markers like: `-----BEGIN PRIVATE KEY-----`) and set the following environmental variables: `RSA_PRIVATE_KEY`, `RSA_PUBLIC_KEY` and `AES_KEY`.

### Encrypt Email & Hash Password for SQL Init File

To insert an **encrypted email** and **hashed password** into the database, follow these steps:

1. Encrypt email with the generated `AES_KEY`
2. Hash password with Argon2
3. Update `02-insert.sql` and add the encrypted email and hashed password.

⚠️**DISCLAIMER**⚠️:
These values are **only for development and testing purposes**. **Do not use personal passwords or sensitive data**, as there is a risk of leaking this information. For testing and development, use common placeholder values such as `test@example.com` and `qwerty` instead.

### Run the backend

#### Run database with Docker (Recommended)

The easiest way to set up the database is with **Docker Compose**.

```
docker-compose up --build
```

This will:
* Initialize the database using `01-schema.sql` and `02-insert.sql` files in the `sql/init` folder.
* Run the database and expose port `3306` to the local machine.

#### Run the Spring Boot REST API

Run the REST API using the following command or from you IDE of choice.

```
./gradlew bootRun
```

## Running tests

Run unit and integration tests using:
```
./gradlew test
```