# Recipe Gallery API

A RESTful API built with Spring Boot and MongoDB for managing a gallery of recipes. Supports full CRUD operations, search by name and ingredients, and image serving.

## Tech Stack

- Java 17
- Spring Boot 3.5.6
- MongoDB Atlas
- Lombok
- Docker

## Getting Started

```bash
git clone https://github.com/SezerEnisBirgili/Recipe-Gallery-API
cd Recipe-Gallery-API
```

---

### Option 1 — Run Locally

1. Open `src/main/resources/application.properties`

2. Get your connection string from MongoDB Atlas → **Drivers → Java 5.1 or higher**

3. Update the following values:

```properties
SPRING_DATA_MONGODB_URI=your_mongodb_connection_string
SPRING_DATA_MONGODB_DATABASE=your_database_name
```

> `your_database_name` is the name of the database inside your MongoDB Atlas cluster. Change it to match whatever database name you created.

4. Run the application:

```bash
.\mvnw spring-boot:run
```

---

### Option 2 — Run with Docker

1. Install and open **Docker Desktop**

2. Create a `.env` file in the root directory

3. Get your connection string from MongoDB Atlas → **Drivers → Java 5.1 or higher**

4. Add the following to your `.env`:

```
SPRING_DATA_MONGODB_URI=your_mongodb_connection_string
SPRING_DATA_MONGODB_DATABASE=your_database_name
```

> `your_database_name` is the name of the database inside your MongoDB Atlas cluster. Change it to match whatever database name you created.

5. Start the application:

```bash
docker compose up
```

---

The API will start at `http://localhost:8080`. If the database is empty, 6 sample recipes are seeded automatically on startup.

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
.\mvnw test
```
