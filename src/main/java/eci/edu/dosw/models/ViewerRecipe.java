package eci.edu.dosw.models;

import eci.edu.dosw.models.enums.ChefType;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "recipes")
public class ViewerRecipe extends Recipe {
  public ViewerRecipe(
      String id,
      String recipeTitle,
      String chefName,
      ChefType chefType,
      List<String> ingredients,
      List<String> instructions) {
    super(id, recipeTitle, chefName, chefType, ingredients, instructions);
  }
}
