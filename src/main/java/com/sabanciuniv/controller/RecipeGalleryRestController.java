package com.sabanciuniv.controller;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sabanciuniv.model.Recipe;
import com.sabanciuniv.repo.RecipeRepository;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/recipegallery")
public class RecipeGalleryRestController {
	
	@Autowired private RecipeRepository recipeRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(RecipeGalleryRestController.class);
	
	@PostConstruct
	public void init() throws IOException, URISyntaxException {
		
		if (recipeRepository.count() == 0) {
			
			logger.info("Database is empty, initializing..");
			
		
			Recipe rec1 = new Recipe("Chicken Salad", 
									 "2 boneless, skinless chicken breasts, 1/2 cup mayonnaise, 1 tablespoon Dijon mustard, 2 tablespoons lemon juice, 1/4 cup chopped celery, 1/4 cup chopped red onion, 1/4 cup chopped pickles, Salt and pepper to taste, Lettuce leaves for serving", 
									 "Cook and dice chicken. Mix mayo, Dijon, lemon juice, veggies, salt, and pepper. Combine chicken with the sauce. Chill for 30 mins. Serve on lettuce.", 
									 "chickenSalad.jpg",
									 "http://10.0.2.2:8080/recipegallery/images/chickenSalad.jpeg");
			
			Recipe rec2 = new Recipe("Strawberry Custard Pancakes", 
									 "1 cup pancake mix, 3/4 cup milk, 1 egg, 1 cup chopped strawberries, 1/2 cup custard, Butter for cooking", 
									 "Mix pancake mix, milk, and egg. Cook pancakes. Layer with chopped strawberries and custard. Serve warm.", 
									 "StrawberryCustardPancakes.jpg",
									 "http://10.0.2.2:8080/recipegallery/images/StrawberryCustardPancakes.jpeg");
			
			Recipe rec3 = new Recipe("Scrambled Eggs", 
									 "4 eggs, 1/4 cup milk, Salt and pepper to taste, 2 tablespoons butter", 
									 "Whisk eggs, milk, salt, and pepper. Melt butter in a pan. Pour in egg mixture. Cook and stir until desired consistency.",
									 "scrambledEggs.jpg",
									 "http://10.0.2.2:8080/recipegallery/images/scrambledEggs.jpeg");
			
			Recipe rec4 = new Recipe("Pepperoni Pizza", 
									 "Pizza dough, Tomato sauce, Shredded mozzarella cheese, Pepperoni slices, Olive oil, Italian seasoning", 
									 "Roll out dough. Spread sauce, cheese, and pepperoni. Drizzle with olive oil. Sprinkle Italian seasoning. Bake until crust is golden.", 
									 "pepperoniPizza.jpg",
									 "http://10.0.2.2:8080/recipegallery/images/pepperoniPizza.jpeg");
			
			Recipe rec5 = new Recipe("Steak", 
									 "2 steaks, Salt and pepper to taste, Olive oil, Garlic cloves, Butter, Fresh herbs (optional)", 
									 "Season steaks with salt and pepper. Sear in hot olive oil. Add garlic and butter. Baste steaks. Rest before serving. Optional: Add fresh herbs.",
									 "steak.jpg",
									 "http://10.0.2.2:8080/recipegallery/images/steak.jpeg");
			
			Recipe rec6 = new Recipe("Waffles", 
									 "1 1/2 cups all-purpose flour, 2 tablespoons sugar, 1 tablespoon baking powder, 1/2 teaspoon salt, 1 1/4 cups milk, 2 eggs, 1/2 cup melted butter, 1 teaspoon vanilla extract", 
									 "Mix dry ingredients. Whisk in milk, eggs, butter, and vanilla. Pour batter into waffle iron. Cook until golden brown.",
									 "waffles.jpg",
									 "http://10.0.2.2:8080/recipegallery/images/waffles.jpeg");
			
			recipeRepository.save(rec1);
			recipeRepository.save(rec2);
			recipeRepository.save(rec3);
			recipeRepository.save(rec4);
			recipeRepository.save(rec5);
			recipeRepository.save(rec6);
			
			logger.info("All sample data saved!");
		}
	}
	
	@GetMapping("/recipes")
	public List<Recipe> recipes(){
		
		return recipeRepository.findAll();
	
	}
	
	@PostMapping("/recipes/save")
	public Recipe saveRecipe(@RequestBody Recipe recipe) {
		
		Recipe recipeSaved = recipeRepository.save(recipe);
		
		return recipeSaved;
	}
	
	@GetMapping("/recipes/search/name")
	public List<Recipe> searchRecipesByName(@RequestParam("name") String name) {
		
	    List<Recipe> recipes = recipeRepository.findByNameContainsIgnoreCase(name);
	    
	    // maybe I want empty list to be returned
	    /*if (recipes.isEmpty()) {
	    	
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No recipes found for the given name");
	    }*/
	    
	    return recipes;
	}

	
	@GetMapping("/recipes/search/ingredients")
	public List<Recipe> searchRecipesByIngredients(@RequestParam("ingredients") String ingredients){
		
		List<Recipe> recipes = recipeRepository.findByDescriptionContainsIgnoreCase(ingredients);
		
		return recipes;
	}
	
	@GetMapping("/recipes/search")
	public List<Recipe> searchRecipes(@RequestParam("search") String word){
		
		
		List<Recipe> recipesByName = searchRecipesByName(word);
		List<Recipe> recipesByIngredients = searchRecipesByIngredients(word);
		
		// give priority to recipesByName in list
		List<Recipe> recipesAll = new ArrayList<>(recipesByName);
		
		recipesAll.addAll(recipesByIngredients);
		
		// remove duplicates
		Set<Recipe> recipesAllset = new LinkedHashSet<>(recipesAll);
		recipesAll.clear();
		
		recipesAll.addAll(recipesAllset);
		
		
		
		return recipesAll;
	}
	
	
	@GetMapping("/recipe/{id}")
	public Recipe getRecipeById(@PathVariable String id) {
		
		Optional<Recipe> foundRecipe = recipeRepository.findById(id);
		
		if(foundRecipe.isPresent()) {
			
			return foundRecipe.get();
			
		} else {
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"recipe not found");
		}	
	}
	
	
	@GetMapping(value = "/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getRecipeImage(@PathVariable String imageName) throws IOException {
		
		ClassPathResource resource = new ClassPathResource("static/images/" + imageName);
		
		if ( resource.exists() ) {
			
			return resource.getContentAsByteArray();
			
		} else {
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"image not found");
		}
	}
}
