## Stock Quote Service

This project uses OpenAPI to document the RESTful APIs. OpenAPI specifications are included in the project and can be accessed as follows:

### Accessing OpenAPI Documentation

- Once the application is running, you can view the API documentation at:
> http://localhost:8080/swagger-ui.html

- This provides an interactive interface where you can explore the available endpoints, their parameters, and responses.
- The APIs are authorized with form login authentication. For simplicity the credentials are,
> - username: user
> - password: userpass
- When APIs are accessed in swagger ui, the browser prompts for credentials. You can use  above credentials.

### Build and Run with Docker Compose

- To build and start the service, run
> docker-compose up --build