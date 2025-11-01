package eci.edu.dosw.controllers;

import eci.edu.dosw.dto.RecipeDTO;
import eci.edu.dosw.dto.UpdateRecipeDTO;
import eci.edu.dosw.models.Recipe;
import eci.edu.dosw.models.enums.ChefType;
import eci.edu.dosw.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/recipes")
@Tag(name = "Recipe Controller", description = "APIs for recipe management")
public class RecipeController {
  private final RecipeService recipeService;

  @GetMapping("/allRecipes")
  @Operation(summary = "Get all recipes", description = "Retrieves all recipes")
  public ResponseEntity<List<Recipe>> getAllRecipes() {
    return ResponseEntity.ok(recipeService.getAllRecipes());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get recipe by ID", description = "Retrieves a recipe by its ID")
  public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
    return ResponseEntity.ok(recipeService.getRecipeById(id));
  }

  @GetMapping("/ChefType/{chefType}")
  @Operation(
      summary = "Get recipes by chef type",
      description = "Retrieves all recipes for a specific chef type (PARTICIPANT, JURY, or VIEWER)")
  public ResponseEntity<List<Recipe>> getRecipesByChefType(@PathVariable ChefType chefType) {
    return ResponseEntity.ok(recipeService.getRecipesByChefType(chefType));
  }

  @GetMapping("/ingredients/{ingredient}")
  @Operation(
      summary = "Get recipes by ingredient",
      description = "Searches for recipes containing a specific ingredient")
  public ResponseEntity<List<Recipe>> getRecipesByIngredient(@PathVariable String ingredient) {
    return ResponseEntity.ok(recipeService.getRecipesByIngredient(ingredient));
  }

  @GetMapping("/season/{season}")
  @Operation(
      summary = "Get recipes by season",
      description = "Searches for recipes by season (only for PARTICIPANT chefs)")
  public ResponseEntity<List<Recipe>> getRecipesBySeason(@PathVariable String season) {
    return ResponseEntity.ok(recipeService.getRecipesBySeason(season));
  }

  @PostMapping("/createRecipe")
  @Operation(summary = "Create recipe", description = "Creates a new recipe")
  public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeDTO recipeCreationRequest) {
    return ResponseEntity.ok(recipeService.createRecipe(recipeCreationRequest));
  }

  @PatchMapping("/{recipeTitle}")
  @Operation(summary = "Update recipe", description = "Updates an existing recipe using its title")
  public ResponseEntity<Recipe> updateRecipe(
      @PathVariable String recipeTitle, @RequestBody UpdateRecipeDTO recipeUpdateRequest) {
    return ResponseEntity.ok(recipeService.updateRecipe(recipeTitle, recipeUpdateRequest));
  }

  @DeleteMapping("/{recipeTitle}")
  @Operation(summary = "Delete recipe", description = "Deletes an existing recipe by title")
  public ResponseEntity<Recipe> deleteRecipe(@PathVariable String recipeTitle) {
    return ResponseEntity.ok(recipeService.deleteRecipe(recipeTitle));
  }
}
