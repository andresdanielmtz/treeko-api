
# Treeko

Simple Spring Boot personal expense tracker with JWT authentication.

## Features

- Register/login with JWT (`Authorization: Bearer <token>`)
- Per-user expense CRUD
- Expense fields: `id`, optional `name`, `cost`, `type` (`PERSONAL`, `SCHOOL`, `WORK`, `OTHER`)

## Tech

- Java 17, Spring Boot, Spring Security, Spring Data JPA
- H2 in-memory database (dev)

## Quickstart

### Run locally

```bash
./mvnw spring-boot:run
```

App starts at `http://localhost:8080`.

### Configure JWT secret (recommended)

Set `JWT_SECRET` (must be long enough for HMAC signing):

```bash
export JWT_SECRET='replace-with-a-long-random-secret'
./mvnw spring-boot:run
```

### Minimal usage (curl)

Register:

```bash
curl -s -X POST http://localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"autumn","email":"autumn@example.com","password":"password1234"}'
```

Create an expense (replace `<jwt>`):

```bash
curl -s -X POST http://localhost:8080/api/expenses \
  -H 'Authorization: Bearer <jwt>' \
  -H 'Content-Type: application/json' \
  -d '{"name":"Lunch","cost":14.50,"type":"PERSONAL"}'
```

## Documentation

- API reference (all routes + JSON examples): [API.md](API.md)
- How to run tests: [TEST.md](TEST.md)

## Dev notes

- H2 console is enabled at `http://localhost:8080/h2-console`
- Database and JWT defaults live in `src/main/resources/application.properties`
