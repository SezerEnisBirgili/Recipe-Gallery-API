package com.recipegalleryapi.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.recipegalleryapi.model.Recipe;

public interface RecipeRepository extends MongoRepository<Recipe, String>{
	
	List<Recipe> findByNameContainsIgnoreCase(String name);
	List<Recipe> findByDescriptionContainsIgnoreCase(String name);
}
