package com.sabanciuniv.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sabanciuniv.model.Recipe;

public interface RecipeRepository extends MongoRepository<Recipe, String>{
	
	public List<Recipe> findByNameContainsIgnoreCase(String name);
	public List<Recipe> findByDescriptionContainsIgnoreCase(String name);
	
	Optional<Recipe> getRecipesById(String id);
}
