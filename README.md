# Recipe Gallery API

> **This is the original version submitted for my Mobile App Development course.**
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

#### 1. Install Java 17 and set `JAVA_HOME`

This project requires **Java 17**

- Download the JDK 17 installer from [adoptium.net](https://adoptium.net)
- During install, check the box to **set `JAVA_HOME`** if the installer offers it.
- If it doesn't, set `JAVA_HOME` manually to the JDK 17 install folder via your OS's environment variable settings, and add `%JAVA_HOME%\bin` to your `PATH`.

#### 2. Create a MongoDB Atlas cluster

- Sign up / log in at [mongodb.com/cloud/atlas](https://www.mongodb.com/cloud/atlas).
- Create a new **free cluster**
- 
#### 3. Create a database user and get your password

- In your cluster's sidebar, go to **Database Access**.
- Click **Add New Database User**, choose a username, and either type your own password or click **Autogenerate Secure Password**.
- **Copy the password and save it somewhere**
- Give the user **Read and write to any database** 

#### 4. Get your connection string

- Go to your cluster → **Connect** → **Drivers**.
- Select **Java** and driver version **5.1 or later**.
- Copy the connection string shown. It looks like:

  ```
  mongodb+srv://<username>:<db_password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0
  ```

- Replace `<db_password>` with the real password from step 3
- 
#### 5. Create `application.properties`

- In `root`, create a new file named `application.properties`
- Add the following:

  ```properties
  spring.data.mongodb.uri=mongodb+srv://<username>:<db_user_password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0
  spring.data.mongodb.database=recipegallery
  ```

  > Double-check there's no trailing whitespace after either value, a stray space at the end of a line is invisible but will break the connection.

#### 6. Run the application

```bash
./mvnw spring-boot:run
```

You should see Spring Boot start up, connect to your Atlas cluster, and seed 6 sample recipes on first run.

---

### Option 2 — Run with Docker

#### 1. Install Docker Desktop

Download and install [Docker Desktop](https://www.docker.com/products/docker-desktop/) and make sure it's running.

#### 2. Create a MongoDB Atlas cluster, database user, and connection string

Follow **steps 2–4 from Option 1 above**

#### 3. Create a `.env` file

In the project root (same folder as `docker-compose.yml`), create a file named `.env` containing:

  ```
  spring.data.mongodb.uri=mongodb+srv://<username>:<db_user_password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0
  spring.data.mongodb.database=recipegallery
  ```

#### 4. Start the application

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
