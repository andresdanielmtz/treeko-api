# Treeko

Personal expense tracker API with JWT authentication.

## Features

- Register/login
- Create, list, update, delete your own expenses

## Documentation

- API reference (all routes + JSON examples): [API.md](API.md)
- How to run tests: [TEST.md](TEST.md)

## How to self host

Prereqs:

- Java 17+

Build and run:

```bash
./mvnw -q clean package
java -jar target/treeko-0.0.1-SNAPSHOT.jar
```

Configuration:

- Set `JWT_SECRET` in your host environment before starting the app.
- Default port is `8080`.
