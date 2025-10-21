package eci.edu.dosw.controllers;

import eci.edu.dosw.dto.RecipeDTO;
import eci.edu.dosw.models.Recipe;
import eci.edu.dosw.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/dean")
@Tag(name = "Dean controller", description = "APIs for dean management")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping("/{recipeName}")
    @Operation(summary = "Get recipe by name", description = "Retrieves a recipe by it's name")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String recipeName) {
        return ResponseEntity.ok(recipeService.getRecipeByName(recipeName));
    }

    @PostMapping("/create")
    @Operation(summary = "Create recipe", description = "Creates a new recipe")
    public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeDTO recipeCreationRequest) {
        return ResponseEntity.ok(recipeService.createRecipe(recipeCreationRequest));
    }

    @PatchMapping("/update/{recipeName}")
    @Operation(summary = "Update recipe", description = "Updates an existing recipe using it's name")
    public ResponseEntity<Recipe> updateRecipe(
            @RequestBody RecipeDTO recipeUpdateRequest, @PathVariable String recipeName) {
        return ResponseEntity.ok(recipeService.updateRecipe(recipeName, recipeUpdateRequest));
    }

    @DeleteMapping("/delete/{recipeName}")
    @Operation(summary = "Delete recipe", description = "Deletes an existing recipe by name")
    public ResponseEntity<Recipe> deleteRecipe(@PathVariable String recipeName) {
        return ResponseEntity.ok(recipeService.deleteRecipe(recipeName));
    }
}