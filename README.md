# library-service

### Tools
- Spring Boot 3.0
- Spring RestClient
- Maven

### Structure
- Main container `library-service` will contain the main pom file, docker related files, postman collections, env and other modules.
- `openlibrary-api-sdk` this module will provide the SDK using RestClient to perform the request to the OpenLibrary API. Currently only the SearchAPI is implemented. This module support spring autoconfigure.
- `library-service-api` module with the spring service that provides all the logic to connect to the OpenLibray API and internal database.

### Deployment

### Run
To run the docker-compose follow the next steps:
1. run the `generate-docker-image.sh`, this step will compile the spring boot project and generate the docker image
2. run the docker compose command: `docker-compose up -d` to start the microservice and postgres
3. to stop: `docker-compose down --volumes`

To run locally
```
mvn clean package -Pdev
```
Inside the docker the profile will be `prod`

### Testing
Postman collection provided on file: [Postman Collection]()
By default the service is running on port `8080`