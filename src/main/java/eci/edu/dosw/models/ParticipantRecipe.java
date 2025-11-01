package eci.edu.dosw.models;

import eci.edu.dosw.models.enums.ChefType;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "recipes")
public class ParticipantRecipe extends Recipe {
  private String season;

  public ParticipantRecipe(
      String id,
      String recipeTitle,
      String chefName,
      ChefType chefType,
      List<String> ingredients,
      List<String> instructions,
      String season) {
    super(id, recipeTitle, chefName, chefType, ingredients, instructions);
    this.season = season;
  }
}
