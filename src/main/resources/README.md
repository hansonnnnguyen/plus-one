# Plus One

Plus One is a social event REST API that helps people meet others through shared interests and local activities. Users can create profiles, host events, join events, view attendees, and manage their participation.

This project is currently under active development.

## Features

- Register and log in with session-based authentication
- Secure password hashing
- Create, view, and update user profiles
- Create, browse, update, and delete events
- Join and leave events
- Prevent duplicate event registrations
- Enforce maximum event capacity
- View an event's attendees
- View events joined by the logged-in user
- View events hosted by the logged-in user
- Display attendee counts and remaining spots
- Validate request data and return structured API errors

## Technologies

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Hibernate
- Maven
- Jakarta Validation
- Postman

## API Endpoints

### Authentication

| Method | Endpoint             | Description             |
| ------ | -------------------- | ----------------------- |
| POST   | `/api/auth/register` | Register a user         |
| POST   | `/api/auth/login`    | Log in                  |
| POST   | `/api/auth/logout`   | Log out                 |
| GET    | `/api/auth/me`       | View the logged-in user |

### Profiles

| Method | Endpoint           | Description         |
| ------ | ------------------ | ------------------- |
| POST   | `/api/profiles`    | Create a profile    |
| GET    | `/api/profiles/me` | View your profile   |
| PUT    | `/api/profiles/me` | Update your profile |

### Events

| Method | Endpoint                | Description            |
| ------ | ----------------------- | ---------------------- |
| POST   | `/api/events`           | Create an event        |
| GET    | `/api/events`           | Browse events          |
| GET    | `/api/events/{eventId}` | View one event         |
| PUT    | `/api/events/{eventId}` | Update a hosted event  |
| DELETE | `/api/events/{eventId}` | Delete a hosted event  |
| GET    | `/api/events/hosted`    | View events you host   |
| GET    | `/api/events/joined`    | View events you joined |

### Attendance

| Method | Endpoint                          | Description    |
| ------ | --------------------------------- | -------------- |
| POST   | `/api/events/{eventId}/join`      | Join an event  |
| DELETE | `/api/events/{eventId}/join`      | Leave an event |
| GET    | `/api/events/{eventId}/attendees` | View attendees |

## Run project locally

### Prerequisites

Install:

- Java 17
- PostgreSQL
- Git

### 1. Clone the repository

```bash
git clone https://github.com/YOUR-USERNAME/plus-one.git
cd plus-one
```

Replace `YOUR-USERNAME` with your GitHub username.

### 2. Create the PostgreSQL database

Create a database named:

```text
plusone
```

### 3. Configure the application

Create:

```text
src/main/resources/application.properties
```

Add your local database configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/plusone
spring.datasource.username=YOUR_DATABASE_USERNAME
spring.datasource.password=YOUR_DATABASE_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Do not commit this file if it contains a real password.

### 4. Start the backend

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

The API runs at:

```text
http://localhost:8080
```

## Project Status

The backend API is currently functional and tested using Postman. A frontend interface and additional automated testing are planned.

## Planned Improvements

- Build the frontend user interface
- Add event search and filtering
- Add profile pictures
- Add automated unit and integration tests
- Improve authentication and production security
- Deploy the API and PostgreSQL database

## Author

Hanson Nguyen  
Computer Science graduate
