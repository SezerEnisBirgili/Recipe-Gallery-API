package com.recipegalleryapi.exception;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(String id) {
        super("Recipe not found with id: " + id);
    }
}
