package eci.edu.dosw.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import eci.edu.dosw.dto.RecipeDTO;
import eci.edu.dosw.dto.UpdateRecipeDTO;
import eci.edu.dosw.expections.BussinessException;
import eci.edu.dosw.models.JuryRecipe;
import eci.edu.dosw.models.ParticipantRecipe;
import eci.edu.dosw.models.Recipe;
import eci.edu.dosw.models.ViewerRecipe;
import eci.edu.dosw.models.enums.ChefType;
import eci.edu.dosw.services.RecipeService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

  @Mock private RecipeService recipeService;

  @InjectMocks private RecipeController recipeController;

  private Recipe testRecipe;
  private RecipeDTO testRecipeDTO;
  private UpdateRecipeDTO testUpdateDTO;

  @BeforeEach
  void setUp() {
    testRecipe =
        new Recipe() {
          @Override
          public String getId() {
            return "1";
          }

          @Override
          public String getRecipeTitle() {
            return "Test Recipe";
          }

          @Override
          public ChefType getChefType() {
            return ChefType.PARTICIPANT;
          }
        };

    testRecipeDTO =
        new RecipeDTO(
            "Test Recipe",
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            "Test Chef",
            "Summer",
            ChefType.PARTICIPANT);

    testUpdateDTO =
        new UpdateRecipeDTO(
            "Updated Recipe",
            Arrays.asList("newIng1", "newIng2"),
            Arrays.asList("newStep1", "newStep2"),
            "Updated Chef");
  }

  @Test
  void getAllRecipes_Success() {
    when(recipeService.getAllRecipes()).thenReturn(Arrays.asList(testRecipe));

    ResponseEntity<List<Recipe>> response = recipeController.getAllRecipes();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    verify(recipeService, times(1)).getAllRecipes();
  }

  @Test
  void getRecipeById_Success() {
    when(recipeService.getRecipeById("1")).thenReturn(testRecipe);

    ResponseEntity<Recipe> response = recipeController.getRecipeById("1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("1", response.getBody().getId());
    verify(recipeService, times(1)).getRecipeById("1");
  }

  @Test
  void getRecipesByChefType_Success() {
    when(recipeService.getRecipesByChefType(ChefType.PARTICIPANT))
        .thenReturn(Arrays.asList(testRecipe));

    ResponseEntity<List<Recipe>> response =
        recipeController.getRecipesByChefType(ChefType.PARTICIPANT);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    verify(recipeService, times(1)).getRecipesByChefType(ChefType.PARTICIPANT);
  }

  @Test
  void getRecipesByIngredient_Success() {
    when(recipeService.getRecipesByIngredient("ing1")).thenReturn(Arrays.asList(testRecipe));

    ResponseEntity<List<Recipe>> response = recipeController.getRecipesByIngredient("ing1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(recipeService, times(1)).getRecipesByIngredient("ing1");
  }

  @Test
  void getRecipesBySeason_Success() {
    when(recipeService.getRecipesBySeason("Summer")).thenReturn(Arrays.asList(testRecipe));

    ResponseEntity<List<Recipe>> response = recipeController.getRecipesBySeason("Summer");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(recipeService, times(1)).getRecipesBySeason("Summer");
  }

  @Test
  void createRecipe_Success() {
    when(recipeService.createRecipe(any(RecipeDTO.class))).thenReturn(testRecipe);

    ResponseEntity<Recipe> response = recipeController.createRecipe(testRecipeDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(recipeService, times(1)).createRecipe(testRecipeDTO);
  }

  @Test
  void updateRecipe_Success() {
    when(recipeService.updateRecipe("Test Recipe", testUpdateDTO)).thenReturn(testRecipe);

    ResponseEntity<Recipe> response = recipeController.updateRecipe("Test Recipe", testUpdateDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(recipeService, times(1)).updateRecipe("Test Recipe", testUpdateDTO);
  }

  @Test
  void deleteRecipe_Success() {
    when(recipeService.deleteRecipe("Test Recipe")).thenReturn(testRecipe);

    ResponseEntity<Recipe> response = recipeController.deleteRecipe("Test Recipe");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(recipeService, times(1)).deleteRecipe("Test Recipe");
  }

  @Test
  void getAllRecipes_EmptyList_Success() {
    when(recipeService.getAllRecipes()).thenReturn(Arrays.asList());

    ResponseEntity<List<Recipe>> response = recipeController.getAllRecipes();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isEmpty());
    verify(recipeService, times(1)).getAllRecipes();
  }

  @Test
  void getRecipesByChefType_EmptyList_Success() {
    when(recipeService.getRecipesByChefType(ChefType.VIEWER)).thenReturn(Arrays.asList());

    ResponseEntity<List<Recipe>> response = recipeController.getRecipesByChefType(ChefType.VIEWER);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isEmpty());
    verify(recipeService, times(1)).getRecipesByChefType(ChefType.VIEWER);
  }

  @Test
  void getRecipeById_ServiceThrowsException_PropagatesException() {
    when(recipeService.getRecipeById("999")).thenThrow(new BussinessException("Recipe not found"));

    assertThrows(BussinessException.class, () -> recipeController.getRecipeById("999"));
  }

  @Test
  void createRecipe_ServiceThrowsException_PropagatesException() {
    when(recipeService.createRecipe(any(RecipeDTO.class)))
        .thenThrow(new BussinessException("Invalid data"));

    assertThrows(BussinessException.class, () -> recipeController.createRecipe(testRecipeDTO));
  }

  @Test
  void updateRecipe_PartialUpdate_Success() {
    UpdateRecipeDTO partialUpdate =
        new UpdateRecipeDTO("Updated Title", null, null, "Updated Chef");

    when(recipeService.updateRecipe("Test Recipe", partialUpdate)).thenReturn(testRecipe);

    ResponseEntity<Recipe> response = recipeController.updateRecipe("Test Recipe", partialUpdate);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(recipeService, times(1)).updateRecipe("Test Recipe", partialUpdate);
  }

  @Test
  void deleteRecipe_ServiceThrowsException_PropagatesException() {
    when(recipeService.deleteRecipe("Nonexistent"))
        .thenThrow(new BussinessException("Recipe not found"));

    assertThrows(BussinessException.class, () -> recipeController.deleteRecipe("Nonexistent"));
  }

  @Test
  void getRecipesBySeason_ParticipantRecipes_Success() {
    ParticipantRecipe seasonalRecipe =
        new ParticipantRecipe(
            "2",
            "Summer Salad",
            "Summer Chef",
            ChefType.PARTICIPANT,
            Arrays.asList("lettuce", "tomato"),
            Arrays.asList("Mix ingredients"),
            "Summer");

    when(recipeService.getRecipesBySeason("Summer")).thenReturn(Arrays.asList(seasonalRecipe));

    ResponseEntity<List<Recipe>> response = recipeController.getRecipesBySeason("Summer");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("Summer Salad", response.getBody().get(0).getRecipeTitle());
  }

  @Test
  void getRecipesByIngredient_MultipleRecipes_Success() {
    Recipe recipe1 =
        new JuryRecipe(
            "3",
            "Pasta Carbonara",
            "Italian Chef",
            ChefType.JURY,
            Arrays.asList("pasta", "eggs", "bacon"),
            Arrays.asList("Cook pasta", "Mix ingredients"));

    Recipe recipe2 =
        new ViewerRecipe(
            "4",
            "Bacon Sandwich",
            "Viewer Chef",
            ChefType.VIEWER,
            Arrays.asList("bread", "bacon", "lettuce"),
            Arrays.asList("Toast bread", "Add bacon"));

    when(recipeService.getRecipesByIngredient("bacon")).thenReturn(Arrays.asList(recipe1, recipe2));

    ResponseEntity<List<Recipe>> response = recipeController.getRecipesByIngredient("bacon");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().size());
    verify(recipeService, times(1)).getRecipesByIngredient("bacon");
  }
}
