# Treeko API

Base URL: `http://localhost:8080`

Authentication: JWT using `Authorization: Bearer <token>`.

## Quick usage (curl)

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

## Auth

### POST /api/auth/register

Creates a new user and returns a JWT.

Request JSON:

```json
{
  "username": "autumn",
  "email": "autumn@example.com",
  "password": "super-secret-password"
}
```

Response JSON:

```json
{
  "token": "<jwt>",
  "tokenType": "Bearer"
}
```

### POST /api/auth/login

Logs in and returns a JWT.

Request JSON:

```json
{
  "username": "autumn",
  "password": "super-secret-password"
}
```

Response JSON:

```json
{
  "token": "<jwt>",
  "tokenType": "Bearer"
}
```

## Users

### GET /api/users/me

Returns the current authenticated user.

Headers:

```text
Authorization: Bearer <jwt>
```

Response JSON:

```json
{
  "id": 1,
  "username": "autumn",
  "email": "autumn@example.com"
}
```

## Expenses

Expense model:

- `id`
- `name` (optional)
- `cost`
- `type` (`PERSONAL`, `SCHOOL`, `WORK`, `OTHER`)

### GET /api/expenses

List your expenses (most recent first).

Headers:

```text
Authorization: Bearer <jwt>
```

Response JSON:

```json
[
  {
    "id": 12,
    "name": "Lunch",
    "cost": 14.50,
    "type": "PERSONAL"
  }
]
```

### GET /api/expenses/{id}

Get a single expense (must belong to you).

Headers:

```text
Authorization: Bearer <jwt>
```

Response JSON:

```json
{
  "id": 12,
  "name": "Lunch",
  "cost": 14.50,
  "type": "PERSONAL"
}
```

### POST /api/expenses

Create an expense.

Headers:

```text
Authorization: Bearer <jwt>
Content-Type: application/json
```

Request JSON:

```json
{
  "name": "Textbook",
  "cost": 89.99,
  "type": "SCHOOL"
}
```

Response JSON (201):

```json
{
  "id": 13,
  "name": "Textbook",
  "cost": 89.99,
  "type": "SCHOOL"
}
```

### PUT /api/expenses/{id}

Replace an existing expense (must belong to you).

Headers:

```text
Authorization: Bearer <jwt>
Content-Type: application/json
```

Request JSON:

```json
{
  "name": "Textbook (used)",
  "cost": 59.99,
  "type": "SCHOOL"
}
```

Response JSON:

```json
{
  "id": 13,
  "name": "Textbook (used)",
  "cost": 59.99,
  "type": "SCHOOL"
}
```

### DELETE /api/expenses/{id}

Delete an expense (must belong to you).

Headers:

```text
Authorization: Bearer <jwt>
```

Response: `204 No Content`

## Errors

Validation errors return `400`:

```json
{
  "timestamp": "2026-02-16T00:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/expenses",
  "fieldErrors": {
    "cost": "must be greater than or equal to 0.01"
  }
}
```

Not found returns `404`.
