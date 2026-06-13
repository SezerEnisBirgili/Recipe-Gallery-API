package com.recipegalleryapi.controller;

import java.io.IOException;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.core.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.recipegalleryapi.model.Recipe;
import com.recipegalleryapi.repo.RecipeRepository;
import com.recipegalleryapi.service.RecipeService;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/recipegallery")
public class RecipeGalleryRestController {

	private final RecipeService recipeService;
	private final RecipeRepository recipeRepository;
	private static final Logger logger = LoggerFactory.getLogger(RecipeGalleryRestController.class);

	public RecipeGalleryRestController(RecipeService recipeService, RecipeRepository recipeRepository) {
		this.recipeService = recipeService;
		this.recipeRepository = recipeRepository;
	}

	@PostConstruct
	public void init() {
		if (recipeRepository.count() == 0) {
			logger.info("Database is empty, initializing..");

			recipeService.saveRecipe(new Recipe("Chicken Salad",
					"2 boneless, skinless chicken breasts, 1/2 cup mayonnaise, 1 tablespoon Dijon mustard, 2 tablespoons lemon juice, 1/4 cup chopped celery, 1/4 cup chopped red onion, 1/4 cup chopped pickles, Salt and pepper to taste, Lettuce leaves for serving",
					"Cook and dice chicken. Mix mayo, Dijon, lemon juice, veggies, salt, and pepper. Combine chicken with the sauce. Chill for 30 mins. Serve on lettuce.",
					"chickenSalad.jpg",
					"http://10.0.2.2:8080/recipegallery/images/chickenSalad.jpeg"));

			recipeService.saveRecipe(new Recipe("Strawberry Custard Pancakes",
					"1 cup pancake mix, 3/4 cup milk, 1 egg, 1 cup chopped strawberries, 1/2 cup custard, Butter for cooking",
					"Mix pancake mix, milk, and egg. Cook pancakes. Layer with chopped strawberries and custard. Serve warm.",
					"StrawberryCustardPancakes.jpg",
					"http://10.0.2.2:8080/recipegallery/images/StrawberryCustardPancakes.jpeg"));

			recipeService.saveRecipe(new Recipe("Scrambled Eggs",
					"4 eggs, 1/4 cup milk, Salt and pepper to taste, 2 tablespoons butter",
					"Whisk eggs, milk, salt, and pepper. Melt butter in a pan. Pour in egg mixture. Cook and stir until desired consistency.",
					"scrambledEggs.jpg",
					"http://10.0.2.2:8080/recipegallery/images/scrambledEggs.jpeg"));

			recipeService.saveRecipe(new Recipe("Pepperoni Pizza",
					"Pizza dough, Tomato sauce, Shredded mozzarella cheese, Pepperoni slices, Olive oil, Italian seasoning",
					"Roll out dough. Spread sauce, cheese, and pepperoni. Drizzle with olive oil. Sprinkle Italian seasoning. Bake until crust is golden.",
					"pepperoniPizza.jpg",
					"http://10.0.2.2:8080/recipegallery/images/pepperoniPizza.jpeg"));

			recipeService.saveRecipe(new Recipe("Steak",
					"2 steaks, Salt and pepper to taste, Olive oil, Garlic cloves, Butter, Fresh herbs (optional)",
					"Season steaks with salt and pepper. Sear in hot olive oil. Add garlic and butter. Baste steaks. Rest before serving. Optional: Add fresh herbs.",
					"steak.jpg",
					"http://10.0.2.2:8080/recipegallery/images/steak.jpeg"));

			recipeService.saveRecipe(new Recipe("Waffles",
					"1 1/2 cups all-purpose flour, 2 tablespoons sugar, 1 tablespoon baking powder, 1/2 teaspoon salt, 1 1/4 cups milk, 2 eggs, 1/2 cup melted butter, 1 teaspoon vanilla extract",
					"Mix dry ingredients. Whisk in milk, eggs, butter, and vanilla. Pour batter into waffle iron. Cook until golden brown.",
					"waffles.jpg",
					"http://10.0.2.2:8080/recipegallery/images/waffles.jpeg"));

			logger.info("All sample data saved!");
		}
	}

	@GetMapping("/recipes")
	public List<Recipe> recipes() {
		return recipeService.getAllRecipes();
	}

	@PostMapping("/recipes/save")
	public Recipe saveRecipe(@Valid @RequestBody Recipe recipe) {
		return recipeService.saveRecipe(recipe);
	}

	@PutMapping("/recipe/{id}")
	public Recipe updateRecipe(@Valid @PathVariable String id, @RequestBody Recipe recipe) {
		return recipeService.updateRecipe(id, recipe);
	}

	@DeleteMapping("/recipe/{id}")
	public void deleteRecipe(@PathVariable String id) {
		recipeService.deleteRecipe(id);
	}

	@GetMapping("/recipes/search/name")
	public List<Recipe> searchRecipesByName(@RequestParam("name") String name) {
		return recipeService.searchByName(name);
	}

	@GetMapping("/recipes/search/ingredients")
	public List<Recipe> searchRecipesByIngredients(@RequestParam("ingredients") String ingredients) {
		return recipeService.searchByIngredients(ingredients);
	}

	@GetMapping("/recipes/search")
	public List<Recipe> searchRecipes(@RequestParam("search") String word) {
		return recipeService.search(word);
	}

	@GetMapping("/recipe/{id}")
	public Recipe getRecipeById(@PathVariable String id) {
		return recipeService.getRecipeById(id);
	}

	@GetMapping(value = "/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getRecipeImage(@PathVariable String imageName) throws IOException {
		ClassPathResource resource = new ClassPathResource("static/images/" + imageName);
		if (resource.exists()) {
			return resource.getContentAsByteArray();
		}

		throw new RuntimeException("Image not found: " + imageName);
	}
}