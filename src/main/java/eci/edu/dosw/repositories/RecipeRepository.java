package eci.edu.dosw.repositories;

import eci.edu.dosw.models.Recipe;
import eci.edu.dosw.models.enums.ChefType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
  @Query("{ 'recipeTitle': ?0}")
  Optional<Recipe> findByRecipeTitle(String recipeTitle);

  @Query("{ 'id': ?0}")
  Optional<Recipe> findByRecipeById(String id);

  @Query("{ 'chefType': ?0}")
  List<Recipe> findByChefType(ChefType chefType);

  @Query(value = "{ 'ingredients.ingredient': ?0}", fields = "{'ingredients.$': 1}")
  List<Recipe> findByRecipesByIngredient(String ingredient);

  @Query("{ 'season': ?0}")
  List<Recipe> findByRecipesBySeason(String season);
}
