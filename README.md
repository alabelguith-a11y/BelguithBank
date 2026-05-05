# FirstSpringBootApplication

A Spring Boot 4 web application built with Spring MVC, Spring Data JPA, Thymeleaf, and MySQL. The project manages two main domain objects:

- `Client`
- `Compte`

The application is packaged as a WAR file and uses Maven Wrapper for builds.

## Features

- CRUD operations for clients
- CRUD operations for comptes
- Server-side rendered pages with Thymeleaf
- MySQL persistence with JPA/Hibernate
- Simple controller-based web layer

## Tech Stack

- Java 25
- Spring Boot 4.0.2
- Spring MVC
- Spring Data JPA
- Thymeleaf
- MySQL Connector/J
- Lombok

## Prerequisites

- JDK 25
- MySQL Server
- Maven Wrapper enabled in the project

## Database Configuration

The application is configured in [src/main/resources/application.properties](src/main/resources/application.properties) to use:

- URL: `jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC&createDatabaseIfNotExist=true`
- Username: `root`
- Password: empty

Hibernate schema generation is set to `update`, so tables will be created or updated automatically on startup.

If your local MySQL setup is different, update the datasource properties before running the app.

## Run the Application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

The application starts on the default Spring Boot port:

```text
http://localhost:8080
```

## Build the Project

```bash
./mvnw clean package
```

On Windows, use `mvnw.cmd` instead of `mvnw`.

Because the project is packaged as a WAR, the build output will be generated under `target/`.

## Main Pages and Endpoints

### Clients

- `GET /clients` - list clients
- `POST /clients/save` - create a client
- `GET /clients/edit/{cin}` - open the edit form
- `POST /clients/update` - update a client
- `POST /clients/delete` - delete a client

### Comptes

- `GET /comptes` - list comptes and available clients
- `POST /comptes/save` - create a compte for a client
- `GET /comptes/edit/{rib}` - open the edit form
- `POST /comptes/update` - update a compte
- `POST /comptes/delete` - delete a compte

## Project Structure

- `src/main/java/tn/enis/controller` - web controllers
- `src/main/java/tn/enis/service` - service layer
- `src/main/java/tn/enis/dao` - JPA repositories
- `src/main/java/tn/enis/entity` - JPA entities
- `src/main/java/tn/enis/dto` - simple data transfer objects
- `src/main/resources/templates` - Thymeleaf views
- `src/main/resources/application.properties` - application configuration

## Notes

- `Client` is identified by `cin`.
- `Compte` uses an auto-generated `rib` primary key.
- A `Client` can have multiple `Compte` records.
