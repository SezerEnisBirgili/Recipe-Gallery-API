package com.sabanciuniv.service;

import com.sabanciuniv.model.Recipe;
import com.sabanciuniv.repo.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));
    }

    public List<Recipe> searchByName(String name) {
        return recipeRepository.findByNameContainsIgnoreCase(name);
    }

    public List<Recipe> searchByIngredients(String ingredients) {
        return recipeRepository.findByDescriptionContainsIgnoreCase(ingredients);
    }

    public List<Recipe> search(String word) {
        List<Recipe> byName = searchByName(word);
        List<Recipe> byIngredients = searchByIngredients(word);

        // name results take priority, duplicates removed via LinkedHashSet
        List<Recipe> combined = new ArrayList<>(byName);
        combined.addAll(byIngredients);

        Set<Recipe> deduped = new LinkedHashSet<>(combined);
        return new ArrayList<>(deduped);
    }

    public void deleteRecipe(String id) {
        if (!recipeRepository.existsById(id)) {
            throw new RuntimeException("Recipe not found with id: " + id);
        }
        recipeRepository.deleteById(id);
    }

    public Recipe updateRecipe(String id, Recipe updated) {
        Recipe existing = getRecipeById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setInstructions(updated.getInstructions());
        existing.setImage(updated.getImage());
        existing.setImagePath(updated.getImagePath());
        return recipeRepository.save(existing);
    }
}
