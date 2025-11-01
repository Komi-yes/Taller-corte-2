package eci.edu.dosw.dto;

import java.util.List;

public record UpdateRecipeDTO(
    String recipeTitle, List<String> ingredients, List<String> instructions, String chefName) {}
