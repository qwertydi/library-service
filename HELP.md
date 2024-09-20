# Challenge

## Overview
Create a RESTful API that lets users search for and add books. 
This API will manage data from different sources and present the information in a unified and simple way to the clients.

## Requirements

### Functional RESTful Endpoints
API must expose the following endpoints:

1. **Get a Book by ID**  
   Retrieve details of a book for a given unique ID.

2. **Get All Books**  
   Retrieve all books. (No pagination is required).

3. **Search Books by Title**  
   Search and retrieve books based on a given title.

4. **Add a Book**  
   Add a new book to your own data source (local database).

### Data Sources

Manage two separate data sources:
1. **Open Library Search API**
    - Requires no authentication.
    - Serves as one of the book data sources.

2. **Local Database**
    - Managed through your own API endpoints.
    - Used to store books added by clients.

### Important Considerations

- API must abstract away the data sources. Clients should not know if a book comes from the Open Library Search API or local database.
- **Response Format:**  
  The response from the API should always be in JSON format with the following fields:
    - **ID**: Unique ID of the book.
    - **Title**: Book title.
    - **Authors**: List of authors.
    - **Publish Year**: The first year the book was published.
    - **Languages**: List of available languages.

  If any of these fields are unavailable for certain books, they should be treated as optional and omitted from the response.

### Additional Features

- **Email Notification:**  
  After a new book is added, an email should be sent to an admin. You don’t need to send a real email. Instead, create a dummy class responsible for simulating the email-sending process.

### Containerization

- The solution must be containerized and run with a simple `docker compose up`.

## Technologies & Tools
- **Kotlin/Java**
- **Open Library Search API** (external data source)
- **Local Database** (for added books)
- **Docker & Docker Compose** (for containerization)
