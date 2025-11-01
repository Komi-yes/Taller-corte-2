package eci.edu.dosw.dto;

import eci.edu.dosw.models.enums.ChefType;
import java.util.List;

public record RecipeDTO(
    String recipeTitle,
    List<String> ingredients,
    List<String> instructions,
    String chefName,
    String season,
    ChefType chefType) {}
