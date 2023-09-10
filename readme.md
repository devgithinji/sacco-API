# Sacco API

This is a simple Spring Boot application that demonstrates how to connect to a MySQL database and perform CRUD operations for a Sacco API.

## Requirements

- Java 17
- Maven
- MySQL

## Setup

1. Clone the repository:

git clone <repo url>

2. Create a MySQL database and user.
3. Update the `application.properties` file in the `src/main/resources` folder with the following information:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/[your-database-name]
spring.datasource.username=[your-username]
spring.datasource.password=[your-password]
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

## Build the project:

`mvn clean install`

## Run the application:

`mvn spring-boot:run`

The application will be available at http://localhost:8080.

## Tests

The application comes with a set of unit tests. To run the tests, execute the following command:


`mvn test`
