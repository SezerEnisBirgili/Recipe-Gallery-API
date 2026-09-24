# Recipe Gallery API

>  **This is the original version submitted for my Mobile App Development course.**
> I've since continued improving this project — see the **[`v2.0` branch](../../tree/v2.0)** for the updated version with a service layer, input validation, custom exception handling, Lombok, and a full test suite.

A RESTful API built with Spring Boot and MongoDB for managing a gallery of recipes. Supports creating and retrieving recipes, searching by name and ingredients, and serving recipe images.

## Tech Stack

- Java 17
- Spring Boot
- MongoDB Atlas
- Maven
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

## Project Structure

```
src/
└── main/java/com/sabanciuniv/
    ├── controller/   # HTTP request handling
    ├── model/         # Recipe entity
    └── repo/          # MongoDB repository
```
