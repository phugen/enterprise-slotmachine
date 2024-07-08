# Slotmachine Enterprise Edition

This project simulates a slot machine like you might find in an arcade. It's written in Java using Spring Boot (hence: Enterprise edition) and uses a PostgreSQL database for persistence.

## Build
To build the application, use the Gradle wrapper. If you are on a Unix system, that means running
```shell
./gradlew build
```

## Running Unit and integration tests
Running all tests is just as easy. Simply run
```
./gradlew test
```
The integration tests depend on the *Docker runtime* because they are using the `Testcontainers` library to transparently prop up a database to test against.

## Starting the slot machine
To run the application, no special setup other than providing a database instance is necessary. This is fairly easy if you have the Docker runtime installed; simply run the following in a terminal:
```
cd docker && docker-compose up
```
Then either run the application straight from your IDE or build a `.jar` and run the application in another terminal like so:
```
./gradlew jar
java -jar build/libs/<jar-name>.jar
```