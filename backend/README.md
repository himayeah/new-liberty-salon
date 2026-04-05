# Backend Run Instructions

This is a Spring Boot application managed with Maven.

## Prerequisites

1.  **Java 17**: Ensure you have Java 17 installed (`java -version`).
2.  **MySQL Database**:
    - You need a MySQL server running on `localhost:3306`.
    - Create a database named `ems`.
    - Ensure the credentials match your `application.properties`:
        - Username: `root`
        - Password: `Himaya123`

## Running the Application

1.  Navigate to the backend directory:
    ```bash
    cd backend
    ```

2.  Run the application using the Maven Wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```

The application will start on port **8010** (as defined in `application.properties`).

## API Documentation

Once running, you can typically access the API at `http://localhost:8010`.
