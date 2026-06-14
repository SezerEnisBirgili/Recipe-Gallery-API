package com.recipegalleryapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipegalleryapi.controller.RecipeGalleryRestController;
import com.recipegalleryapi.exception.RecipeNotFoundException;
import com.recipegalleryapi.model.Recipe;
import com.recipegalleryapi.repo.RecipeRepository;
import com.recipegalleryapi.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(RecipeGalleryRestController.class)
public class RecipeGalleryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @MockitoBean
    private RecipeRepository recipeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getRecipes_returnsList() throws Exception {
        List<Recipe> recipes = List.of(
                new Recipe("Pasta", "ingredients", "instructions", "pasta.jpg", "url"),
                new Recipe("Salad", "ingredients", "instructions", "salad.jpg", "url")
        );
        when(recipeService.getAllRecipes()).thenReturn(recipes);

        mockMvc.perform(get("/recipegallery/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Pasta"));
    }

    @Test
    void getRecipeById_found_returnsRecipe() throws Exception {
        Recipe recipe = new Recipe("Pasta", "ingredients", "instructions", "pasta.jpg", "url");

        when(recipeService.getRecipeById("123")).thenReturn(recipe);

        mockMvc.perform(get("/recipegallery/recipe/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pasta"));
    }

    @Test
    void getRecipeById_notFound_returns404() throws Exception {
        when(recipeService.getRecipeById("123")).thenThrow(new RecipeNotFoundException("123"));

        mockMvc.perform(get("/recipegallery/recipe/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void saveRecipe_validInput_returnsCreatedRecipe() throws Exception {
        Recipe recipe = new Recipe("Pasta", "ingredients", "instructions", "pasta.jpg", "url");

        when(recipeService.saveRecipe(recipe)).thenReturn(recipe);

        mockMvc.perform(post("/recipegallery/recipes/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pasta"));
    }

    @Test
    void updateRecipe_validInput_returnsUpdatedRecipe() throws Exception {
        Recipe updated = new Recipe("Updated Pasta", "new ingredients", "new instructions", "pasta.jpg", "url");
        when(recipeService.updateRecipe(eq("123"), any(Recipe.class))).thenReturn(updated);

        mockMvc.perform(put("/recipegallery/recipe/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Pasta"));
    }

    @Test
    void deleteRecipe_exists_returns200() throws Exception {
        doNothing().when(recipeService).deleteRecipe("123");

        mockMvc.perform(delete("/recipegallery/recipe/123"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRecipe_notFound_returns404() throws Exception {
        doThrow(new RecipeNotFoundException("999")).when(recipeService).deleteRecipe("999");

        mockMvc.perform(delete("/recipegallery/recipe/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchByName_returnsResults() throws Exception {
        List<Recipe> results = List.of(new Recipe("Chicken Salad", "chicken", "instructions", "img.jpg", "url"));
        when(recipeService.searchByName("chicken")).thenReturn(results);

        mockMvc.perform(get("/recipegallery/recipes/search/name?name=chicken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchByIngredients_returnsResults() throws Exception {
        List<Recipe> results = List.of(new Recipe("Pasta", "tomato", "instructions", "img.jpg", "url"));
        when(recipeService.searchByIngredients("tomato")).thenReturn(results);

        mockMvc.perform(get("/recipegallery/recipes/search/ingredients?ingredients=tomato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void search_returnsResults() throws Exception {
        List<Recipe> results = List.of(new Recipe("Chicken Pasta", "chicken", "instructions", "img.jpg", "url"));
        when(recipeService.search("chicken")).thenReturn(results);

        mockMvc.perform(get("/recipegallery/recipes/search?search=chicken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
