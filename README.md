# User Service API

## Overview

This project provides a RESTful API for managing users, including creating, updating, deleting (soft delete), restoring deleted users, and retrieving user information. The API is built using Spring Boot, WebFlux, and R2DBC for reactive programming with a PostgreSQL/MySQL database.

## Features

- **Create User:** Create a new user.
- **Update User:** Update details of an existing user.
- **Delete User:** Soft delete a user by marking them as deleted.
- **Restore User:** Restore a previously deleted user.
- **Get All Users:** Retrieve all users who are not deleted.
- **Get Deleted Users:** Retrieve all users who are marked as deleted.
- **Search User:** Search for a user by ID, MSISDN, or document number.

## Endpoints

### Create User
```
POST /api/v1/users/create
```
Request Body:
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "msisdn": "1234567890",
  "documentType": "Passport",
  "documentNumber": "A12345678"
}
```

### Update User
```
PUT /api/v1/users/update
```
Request Parameters:
- `id` (Long): User ID

Request Body:
```json
{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "msisdn": "0987654321",
  "documentType": "ID",
  "documentNumber": "B87654321"
}
```

### Delete User
```
DELETE /api/v1/users/delete
```
Request Parameters:
- `id` (Long): User ID

### Restore User
```
PUT /api/v1/users/restore
```
Request Parameters:
- `id` (Long): User ID

### Get All Users
```
GET /api/v1/users/all
```

### Get Deleted Users
```
GET /api/v1/users/deleted
```

### Search User
```
POST /api/v1/users/search
```
Request Body:
```json
{
  "userId": 1,
  "msisdn": "1234567890",
  "documentNumber": "A12345678"
}
```
Note: Only one search criteria is required.

## Normalization Function

### `normalizeMSISDN`
This function normalizes an MSISDN to a standard format:
- Removes the "+" if it starts with "+254"
- Replaces the leading "0" with "254" if it starts with "0"
- Returns the MSISDN unchanged if it starts with "254" or any other case

## Error Handling

- Duplicate Entry: Returns a 400 response with a message indicating the failure due to a duplicate entry.
- Invalid SQL Syntax: Returns a 400 response with a message indicating invalid SQL syntax.
- General Errors: Returns a 500 response with a general error message.

## Prerequisites

- Java 17
- Spring Boot 3.2
- MySQL
- R2DBC
- Maven

## Setup

1. Clone the repository.
2. Update `application.properties` with your database configuration.
3. Run `mvn clean install` to build the project.
4. Start the application using `mvn spring-boot:run`.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Contact

For further information or issues, please contact [muchirinephat@gmail.com].