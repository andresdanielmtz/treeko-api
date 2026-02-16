# Running tests

Prereqs:

- Java 17+

This project uses Maven Wrapper, so you can run tests without installing Maven globally. **You don't need to be running the project for it to run the tests.**

## Run all tests

```bash
./mvnw test
```

## Run a single test class

```bash
./mvnw -Dtest=AuthIntegrationTest test
./mvnw -Dtest=ExpenseIntegrationTest test
```

## Run a single test method

```bash
./mvnw -Dtest=ExpenseIntegrationTest#canCreateListGetUpdateDeleteExpense test
```

## Where to find reports

Surefire reports are written to:

- `target/surefire-reports/`
