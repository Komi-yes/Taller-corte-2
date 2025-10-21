package eci.edu.dosw.services;

import eci.edu.dosw.dto.RecipeDTO;
import eci.edu.dosw.dto.UpdateRecipeDTO;
import eci.edu.dosw.expections.BussinessException;
import eci.edu.dosw.models.JuryRecipe;
import eci.edu.dosw.models.ParticipantRecipe;
import eci.edu.dosw.models.Recipe;
import eci.edu.dosw.models.ViewerRecipe;
import eci.edu.dosw.models.enums.ChefType;
import eci.edu.dosw.repositories.RecipeRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecipeService {
    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);
    private final RecipeRepository recipeRepository;


    public List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(String Id){
        Recipe recipe = recipeRepository.findByRecipeById(Id).orElse(null);
        if(recipe == null){
            logger.error("Recipe not found");
            throw new BussinessException("Recipe not found");
        }
        return recipe;
    }

    public List<Recipe> getRecipesByChefType(ChefType chefType){
        List<Recipe> recipes = recipeRepository.findByChefType(chefType);
        if (recipes == null){
            logger.error("Recipes not found");
            throw new BussinessException("Recipes not found");
        }
        return recipes;
    }

    public List<Recipe> getRecipesByIngredient(String ingredient){
        List<Recipe> recipes = recipeRepository.findByRecipesByIngredient((ingredient));
        if(recipes == null){
            logger.error("Recipes not found with the ingredient written");
            throw new BussinessException("Recipes not found with the ingredient written");
        }
        return recipes;
    }

    public List<Recipe> getRecipesBySeason(String season){
        List<Recipe> recipes = recipeRepository.findByRecipesBySeason((season));
        if(recipes == null){
            logger.error("Recipes not found with the season written");
            throw new BussinessException("Recipes not found with the season written");
        }
        return recipes;
    }

    public Recipe updateRecipe(String recipeTitle, UpdateRecipeDTO recipeUpdateRequest) {
        Recipe recipe = recipeRepository.findByRecipeTitle(recipeTitle).orElse(null);
        if (recipe == null) {
            logger.error("Recipe not found");
            throw new BussinessException("Recipe not found");
        }

        if (recipeUpdateRequest.recipeTitle() != null)
            recipe.setRecipeTitle(recipeUpdateRequest.recipeTitle());
        if (recipeUpdateRequest.ingredients() != null)
            recipe.setIngredients(recipeUpdateRequest.ingredients());
        if (recipeUpdateRequest.instructions() != null)
            recipe.setInstructions(recipeUpdateRequest.instructions());
        if (recipeUpdateRequest.chefName() != null)
            recipe.setChefName(recipeUpdateRequest.chefName());

        try {
            return recipeRepository.save(recipe);
        } catch (Exception e) {
            logger.error("Error updating recipe: {}", e.getMessage(), e);
            throw new BussinessException(
                    "An inesperated error has occurred when updating the recipe: " + e.getMessage());
        }
    }

    public Recipe deleteRecipe(String recipeTitle) {
        Recipe recipe = recipeRepository.findByRecipeTitle(recipeTitle).orElse(null);
        if (recipe == null) {
            logger.error("Recipe not found");
            throw new BussinessException("Recipe not found");
        }
        try {
            recipeRepository.delete(recipe);
            return recipe;
        } catch (Exception e) {
            throw new BussinessException(
                    "An inesperated error has occurred when deleting the recipe: " + e.getMessage());
        }
    }


    public Recipe createRecipe(RecipeDTO recipeCreationRequest) {
        if(recipeCreationRequest.recipeTitle() == null){
            logger.error("Recipe title cannot be null");
            throw new BussinessException("Recipe title cannot be null");
        }
        if(recipeCreationRequest.ingredients() == null){
            logger.error("Ingredients cannot be null");
            throw new BussinessException("Ingredients cannot be null");
        }
        if(recipeCreationRequest.instructions() == null){
            logger.error("Instructions cannot be null");
            throw new BussinessException("Instructions cannot be null");
        }
        if(recipeCreationRequest.chefType() != ChefType.PARTICIPANT && recipeCreationRequest.season() != null){
            logger.error("Chef is not a participant and was send with a season");
            throw new BussinessException("Chef is not a participant and was send with a season");
        }
        try{
            ChefType.valueOf(recipeCreationRequest.chefType().toString());
        } catch (RuntimeException e) {
            logger.error("Unsupported role: {}", recipeCreationRequest.chefType());
            throw new BussinessException("Unsupported role: " + recipeCreationRequest.chefType());
        }

        Recipe recipe;
        if(recipeCreationRequest.chefType() == ChefType.PARTICIPANT){
            recipe = new ParticipantRecipe(
                    recipeCreationRequest.recipeTitle(),
                    recipeCreationRequest.chefName(),
                    recipeCreationRequest.chefType(),
                    recipeCreationRequest.ingredients(),
                    recipeCreationRequest.instructions(),
                    recipeCreationRequest.season()
            );
        }
        else if(recipeCreationRequest.chefType() == ChefType.JURY){
            recipe = new JuryRecipe(
                    recipeCreationRequest.recipeTitle(),
                    recipeCreationRequest.chefName(),
                    recipeCreationRequest.chefType(),
                    recipeCreationRequest.ingredients(),
                    recipeCreationRequest.instructions()
            );
        }
        else {
            recipe = new ViewerRecipe(
                    recipeCreationRequest.recipeTitle(),
                    recipeCreationRequest.chefName(),
                    recipeCreationRequest.chefType(),
                    recipeCreationRequest.ingredients(),
                    recipeCreationRequest.instructions()
            );

        }
        try {
            return recipeRepository.save(recipe);
        } catch (Exception e) {
            logger.error("An inesperated error has occurred when creating the recipe: " + e.getMessage());
            throw new BussinessException("An inesperated error has occurred when creating the recipe: " + e.getMessage());
        }
    }
}