package eci.edu.dosw.models;

import eci.edu.dosw.models.enums.ChefType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "recipes")
public class ParticipantRecipe extends Recipe{
    private String season;
    public ParticipantRecipe(String id, String recipeTitle, String chefName, ChefType chefType, List<String> ingredients, List<String> instructions, String season) {
        super(id, recipeTitle, chefName, chefType, ingredients, instructions);
        this.season = season;
    }
}
