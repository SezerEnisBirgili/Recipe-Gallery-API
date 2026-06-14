package com.recipegalleryapi;


import com.recipegalleryapi.exception.RecipeNotFoundException;
import com.recipegalleryapi.model.Recipe;
import com.recipegalleryapi.repo.RecipeRepository;
import com.recipegalleryapi.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void getAllRecipes_returnsList() {
        List<Recipe> mockRecipes = List.of(
                new Recipe("Pasta", "ingredients", "instructions", "pasta.jpg", "url"),
                new Recipe("Salad", "ingredients", "instructions", "salad.jpg", "url")
        );

        when(recipeRepository.findAll()).thenReturn(mockRecipes);

        List<Recipe> result = recipeService.getAllRecipes();

        assertEquals(2, result.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void getAllRecipes_returnsEmptyList() {
        when(recipeRepository.findAll()).thenReturn(List.of());

        List<Recipe> result = recipeService.getAllRecipes();

        assertEquals(0, result.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void getRecipeById_found_returnsRecipe() {
        Recipe mock = new Recipe("Pasta", "ingredients", "instructions", "pasta.jpg", "url");

        when(recipeRepository.findById("123")).thenReturn(Optional.of(mock));

        Recipe result = recipeService.getRecipeById("123");

        assertEquals("Pasta", result.getName());
    }

    @Test
    void getRecipeById_notFound_throwsException() {
        when(recipeRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> recipeService.getRecipeById("999"));
    }

    @Test
    void saveRecipe_returnsSavedRecipe() {
        Recipe recipe = new Recipe("Pasta", "ingredients", "instructions", "pasta.jpg", "url");
        when(recipeRepository.save(recipe)).thenReturn(recipe);

        Recipe result = recipeService.saveRecipe(recipe);

        assertEquals("Pasta", result.getName());
        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    void deleteRecipe_exists_deletesSuccessfully() {
        when(recipeRepository.existsById("123")).thenReturn(true);

        recipeService.deleteRecipe("123");

        verify(recipeRepository, times(1)).deleteById("123");
    }

    @Test
    void deleteRecipe_notFound_throwsException() {
        when(recipeRepository.existsById("999")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> recipeService.deleteRecipe("999"));
    }

    @Test
    void searchByName_returnsMatchingRecipes() {
        List<Recipe> mockResults = List.of(new Recipe("Chicken Salad", "chicken", "instructions", "img.jpg", "url"));
        when(recipeRepository.findByNameContainsIgnoreCase("chicken")).thenReturn(mockResults);

        List<Recipe> result = recipeService.searchByName("chicken");

        assertEquals(1, result.size());
        assertEquals("Chicken Salad", result.get(0).getName());
        verify(recipeRepository, times(1)).findByNameContainsIgnoreCase("chicken");
    }

    @Test
    void searchByName_noMatch_returnsEmptyList() {
        when(recipeRepository.findByNameContainsIgnoreCase("xyz")).thenReturn(List.of());

        List<Recipe> result = recipeService.searchByName("xyz");

        assertEquals(0, result.size());
    }

    @Test
    void searchByIngredients_returnsMatchingRecipes() {
        List<Recipe> mockResults = List.of(
                new Recipe("Pasta", "tomato sauce", "instructions", "img.jpg", "url"),
                new Recipe("Salad", "tomato", "instructions", "img.jpg", "url"));

        when(recipeRepository.findByDescriptionContainsIgnoreCase("tomato")).thenReturn(mockResults);

        List<Recipe> result = recipeService.searchByIngredients("tomato");

        assertEquals(2, result.size());
        verify(recipeRepository, times(1)).findByDescriptionContainsIgnoreCase("tomato");
    }

    @Test
    void searchByIngredients_noMatch_returnsEmptyList() {
        when(recipeRepository.findByDescriptionContainsIgnoreCase("xyz")).thenReturn(List.of());

        List<Recipe> result = recipeService.searchByIngredients("xyz");

        assertEquals(0, result.size());
    }

    @Test
    void search_nameResultsComeBefore_ingredientResults() {
        Recipe byName = new Recipe("Chicken Soup", "water", "instructions", "img.jpg", "url");
        Recipe byIngredient = new Recipe("Pasta", "chicken broth", "instructions", "img.jpg", "url");

        when(recipeRepository.findByNameContainsIgnoreCase("chicken")).thenReturn(List.of(byName));
        when(recipeRepository.findByDescriptionContainsIgnoreCase("chicken")).thenReturn(List.of(byIngredient));

        List<Recipe> result = recipeService.search("chicken");

        assertEquals(2, result.size());
        assertEquals("Chicken Soup", result.get(0).getName());
        assertEquals("Pasta", result.get(1).getName());
    }

    @Test
    void updateRecipe_updatesAllFields() {
        Recipe existing = new Recipe("Old Name", "old desc", "old instructions", "old.jpg", "old-url");
        Recipe updated = new Recipe("New Name", "new desc", "new instructions", "new.jpg", "new-url");

        when(recipeRepository.findById("123")).thenReturn(Optional.of(existing));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(updated);

        Recipe result = recipeService.updateRecipe("123", updated);

        assertEquals("New Name", result.getName());
        assertEquals("new desc", result.getDescription());
        assertEquals("new instructions", result.getInstructions());
        assertEquals("new.jpg", result.getImage());
        assertEquals("new-url", result.getImagePath());
        verify(recipeRepository, times(1)).save(existing);
    }

    @Test
    void updateRecipe_notFound_throwsException() {
        Recipe updated = new Recipe("New Name", "new desc", "new instructions", "new.jpg", "new-url");
        when(recipeRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe("999", updated));
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void deleteRecipe_callsDeleteById_withCorrectId() {
        when(recipeRepository.existsById("123")).thenReturn(true);

        recipeService.deleteRecipe("123");

        verify(recipeRepository, times(1)).deleteById("123");
        verify(recipeRepository, never()).deleteById("999");
    }

    @Test
    void getRecipeById_throwsRecipeNotFoundException_notGenericException() {
        when(recipeRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById("999"));
    }

    @Test
    void search_returnsCombinedResultsWithoutDuplicates() {
        Recipe r1 = new Recipe("Chicken Salad", "chicken", "instructions", "img.jpg", "url");
        Recipe r2 = new Recipe("Pasta", "chicken broth", "instructions", "img.jpg", "url");

        when(recipeRepository.findByNameContainsIgnoreCase("chicken")).thenReturn(List.of(r1));
        when(recipeRepository.findByDescriptionContainsIgnoreCase("chicken")).thenReturn(List.of(r1, r2));

        List<Recipe> result = recipeService.search("chicken");

        assertEquals(2, result.size());
        assertEquals("Chicken Salad", result.get(0).getName());
    }
}
