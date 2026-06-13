# Recipe Gallery API

A RESTful API built with Spring Boot and MongoDB for managing a gallery of recipes. Supports full CRUD operations, search by name and ingredients, and image serving.

## Tech Stack

- Java 17
- Spring Boot 3.5.6
- MongoDB Atlas
- Lombok
- Docker

## Getting Started

### Prerequisites

- Java 17
- Maven
- Docker (optional)

### Run Locally

1. Clone the repository
2. Create an `.env` file in the root directory (see Environment Variables below)
3. Run the application:

```bash
mvn spring-boot:run
```

The API will start at `http://localhost:8080`. If the database is empty, 6 sample recipes are seeded automatically on startup.

### Run with Docker

```bash
docker-compose up --build
```

To reuse existing containers without rebuilding:

```bash
docker-compose up
```

## Environment Variables

Create a `.env` file in the root directory:

```
SPRING_DATA_MONGODB_URI=your_mongodb_connection_string
SPRING_DATA_MONGODB_DATABASE=recipegallery
```

## API Endpoints

### Recipes

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/recipegallery/recipes` | Get all recipes |
| GET | `/recipegallery/recipe/{id}` | Get recipe by ID |
| POST | `/recipegallery/recipes/save` | Create a new recipe |
| PUT | `/recipegallery/recipe/{id}` | Update a recipe |
| DELETE | `/recipegallery/recipe/{id}` | Delete a recipe |

### Search

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/recipegallery/recipes/search?search={word}` | Search by name and ingredients |
| GET | `/recipegallery/recipes/search/name?name={name}` | Search by name |
| GET | `/recipegallery/recipes/search/ingredients?ingredients={ingredient}` | Search by ingredient |

### Images

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/recipegallery/images/{imageName}` | Serve a recipe image |

## Example Requests

**Get all recipes:**
```
GET http://localhost:8080/recipegallery/recipes
```

**Create a recipe:**
```
POST http://localhost:8080/recipegallery/recipes/save
Content-Type: application/json

{
  "name": "Pasta",
  "description": "200g pasta, tomato sauce, parmesan",
  "instructions": "Boil pasta. Add sauce. Serve with parmesan.",
  "image": "pasta.jpg",
  "imagePath": "http://localhost:8080/recipegallery/images/pasta.jpg"
}
```

**Search recipes:**
```
GET http://localhost:8080/recipegallery/recipes/search?search=chicken
```

**Update a recipe:**
```
PUT http://localhost:8080/recipegallery/recipe/{id}
Content-Type: application/json

{
  "name": "Updated Name",
  "description": "Updated ingredients",
  "instructions": "Updated instructions",
  "image": "image.jpg",
  "imagePath": "http://localhost:8080/recipegallery/images/image.jpg"
}
```

**Delete a recipe:**
```
DELETE http://localhost:8080/recipegallery/recipe/{id}
```

## Project Structure

```
src/
├── main/java/com/recipegalleryapi/
│   ├── controller/   # HTTP request handling
│   ├── service/      # Business logic
│   ├── repo/         # MongoDB repository
│   ├── model/        # Recipe entity
│   └── exception/    # Global exception handling
└── test/             # Unit tests
```

## Running Tests

```bash
mvn test
```
