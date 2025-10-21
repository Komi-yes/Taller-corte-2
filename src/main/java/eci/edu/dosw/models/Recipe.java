package eci.edu.dosw.models;

import eci.edu.dosw.models.enums.ChefType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "recipes")
public abstract class Recipe {
    @Id
    protected String id;
    protected String recipeTitle;
    protected String chefName;
    protected ChefType chefType;
    protected List<String> ingredients;
    protected List<String> instructions;
    public Recipe(String recipeTitle, String chefName, ChefType chefType, List<String> ingredients, List<String> instructions) {
        this.recipeTitle = recipeTitle;
        this.chefName = chefName;
        this.chefType = chefType;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
}
