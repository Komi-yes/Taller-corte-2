package eci.edu.dosw;

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
import eci.edu.dosw.repositories.RecipeRepository;
import eci.edu.dosw.services.RecipeService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

  @Mock private RecipeRepository recipeRepository;

  @InjectMocks private RecipeService recipeService;

  private Recipe testRecipe;
  private RecipeDTO testRecipeDTO;
  private UpdateRecipeDTO testUpdateDTO;

  @BeforeEach
  void setUp() {
    testRecipe =
        new ParticipantRecipe(
            "1",
            "Test Recipe",
            "Test Chef",
            ChefType.PARTICIPANT,
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            "Summer");

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
    when(recipeRepository.findAll()).thenReturn(Arrays.asList(testRecipe));

    List<Recipe> result = recipeService.getAllRecipes();

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(recipeRepository, times(1)).findAll();
  }

  @Test
  void getRecipeById_Success() {
    when(recipeRepository.findByRecipeById("1")).thenReturn(Optional.of(testRecipe));

    Recipe result = recipeService.getRecipeById("1");

    assertNotNull(result);
    assertEquals("1", result.getId());
    verify(recipeRepository, times(1)).findByRecipeById("1");
  }

  @Test
  void getRecipeById_NotFound() {
    when(recipeRepository.findByRecipeById("1")).thenReturn(Optional.empty());

    assertThrows(BussinessException.class, () -> recipeService.getRecipeById("1"));
    verify(recipeRepository, times(1)).findByRecipeById("1");
  }

  @Test
  void getRecipesByChefType_Success() {
    when(recipeRepository.findByChefType(ChefType.PARTICIPANT))
        .thenReturn(Arrays.asList(testRecipe));

    List<Recipe> result = recipeService.getRecipesByChefType(ChefType.PARTICIPANT);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(recipeRepository, times(1)).findByChefType(ChefType.PARTICIPANT);
  }

  @Test
  void getRecipesByChefType_NotFound() {
    when(recipeRepository.findByChefType(ChefType.PARTICIPANT)).thenReturn(null);

    assertThrows(
        BussinessException.class, () -> recipeService.getRecipesByChefType(ChefType.PARTICIPANT));
  }

  @Test
  void createRecipe_Success() {
    when(recipeRepository.findAll()).thenReturn(Arrays.asList());
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    Recipe result = recipeService.createRecipe(testRecipeDTO);

    assertNotNull(result);
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }

  @Test
  void createRecipe_InvalidChefType() {
    RecipeDTO invalidDTO =
        new RecipeDTO(
            "Test", Arrays.asList("ing1"), Arrays.asList("step1"), "Chef", "Summer", null);

    assertThrows(BussinessException.class, () -> recipeService.createRecipe(invalidDTO));
  }

  @Test
  void createRecipe_SeasonForNonParticipant() {
    RecipeDTO invalidDTO =
        new RecipeDTO(
            "Test", Arrays.asList("ing1"), Arrays.asList("step1"), "Chef", "Summer", ChefType.JURY);

    assertThrows(BussinessException.class, () -> recipeService.createRecipe(invalidDTO));
  }

  @Test
  void updateRecipe_Success() {
    when(recipeRepository.findByRecipeTitle("Test Recipe")).thenReturn(Optional.of(testRecipe));
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    Recipe result = recipeService.updateRecipe("Test Recipe", testUpdateDTO);

    assertNotNull(result);
    verify(recipeRepository, times(1)).save(testRecipe);
  }

  @Test
  void updateRecipe_NotFound() {
    when(recipeRepository.findByRecipeTitle("Nonexistent")).thenReturn(Optional.empty());

    assertThrows(
        BussinessException.class, () -> recipeService.updateRecipe("Nonexistent", testUpdateDTO));
  }

  @Test
  void deleteRecipe_Success() {
    when(recipeRepository.findByRecipeTitle("Test Recipe")).thenReturn(Optional.of(testRecipe));

    Recipe result = recipeService.deleteRecipe("Test Recipe");

    assertNotNull(result);
    verify(recipeRepository, times(1)).delete(testRecipe);
  }

  @Test
  void deleteRecipe_NotFound() {
    when(recipeRepository.findByRecipeTitle("Nonexistent")).thenReturn(Optional.empty());

    assertThrows(BussinessException.class, () -> recipeService.deleteRecipe("Nonexistent"));
  }

  @Test
  void createRecipe_WithNullTitle_ThrowsException() {
    RecipeDTO invalidDTO =
        new RecipeDTO(
            null,
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            "Test Chef",
            null,
            ChefType.JURY);

    assertThrows(BussinessException.class, () -> recipeService.createRecipe(invalidDTO));
  }

  @Test
  void createRecipe_WithNullIngredients_ThrowsException() {
    RecipeDTO invalidDTO =
        new RecipeDTO(
            "Test Recipe", null, Arrays.asList("step1", "step2"), "Test Chef", null, ChefType.JURY);

    assertThrows(BussinessException.class, () -> recipeService.createRecipe(invalidDTO));
  }

  @Test
  void createRecipe_WithNullInstructions_ThrowsException() {
    RecipeDTO invalidDTO =
        new RecipeDTO(
            "Test Recipe", Arrays.asList("ing1", "ing2"), null, "Test Chef", null, ChefType.JURY);

    assertThrows(BussinessException.class, () -> recipeService.createRecipe(invalidDTO));
  }

  @Test
  void createRecipe_ParticipantWithSeason_Success() {
    RecipeDTO participantDTO =
        new RecipeDTO(
            "Participant Recipe",
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            "Participant Chef",
            "Summer",
            ChefType.PARTICIPANT);

    ParticipantRecipe participantRecipe =
        new ParticipantRecipe(
            "part-1",
            "Participant Recipe",
            "Participant Chef",
            ChefType.PARTICIPANT,
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            "Summer");

    when(recipeRepository.findAll()).thenReturn(Arrays.asList());
    when(recipeRepository.save(any(ParticipantRecipe.class))).thenReturn(participantRecipe);

    Recipe result = recipeService.createRecipe(participantDTO);

    assertNotNull(result);
    assertInstanceOf(ParticipantRecipe.class, result);
    assertEquals("Participant Recipe", result.getRecipeTitle());
    assertEquals("Participant Chef", result.getChefName());
    verify(recipeRepository, times(1)).save(any(ParticipantRecipe.class));
  }

  @Test
  void createRecipe_JuryWithoutSeason_Success() {
    RecipeDTO juryDTO =
        new RecipeDTO(
            "Jury Recipe",
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            "Jury Chef",
            null,
            ChefType.JURY);

    JuryRecipe juryRecipe =
        new JuryRecipe(
            "jury-1",
            "Jury Recipe",
            "Jury Chef",
            ChefType.JURY,
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"));

    when(recipeRepository.findAll()).thenReturn(Arrays.asList());
    when(recipeRepository.save(any(JuryRecipe.class))).thenReturn(juryRecipe);

    Recipe result = recipeService.createRecipe(juryDTO);

    assertNotNull(result);
    assertInstanceOf(JuryRecipe.class, result);
    assertEquals("Jury Recipe", result.getRecipeTitle());
    assertEquals("Jury Chef", result.getChefName());
  }

  @Test
  void createRecipe_ViewerWithoutSeason_Success() {
    RecipeDTO viewerDTO =
        new RecipeDTO(
            "Viewer Recipe",
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            "Viewer Chef",
            null,
            ChefType.VIEWER);

    ViewerRecipe viewerRecipe =
        new ViewerRecipe(
            "viewer-1",
            "Viewer Recipe",
            "Viewer Chef",
            ChefType.VIEWER,
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"));

    when(recipeRepository.findAll()).thenReturn(Arrays.asList());
    when(recipeRepository.save(any(ViewerRecipe.class))).thenReturn(viewerRecipe);

    Recipe result = recipeService.createRecipe(viewerDTO);

    assertNotNull(result);
    assertInstanceOf(ViewerRecipe.class, result);
    verify(recipeRepository, times(1)).save(any(ViewerRecipe.class));
  }

  @Test
  void createRecipe_NonParticipantWithSeason_ThrowsException() {
    RecipeDTO invalidDTO =
        new RecipeDTO(
            "Jury Recipe",
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            "Jury Chef",
            "Summer",
            ChefType.JURY);

    assertThrows(BussinessException.class, () -> recipeService.createRecipe(invalidDTO));
  }

  @Test
  void updateRecipe_PartialUpdate_Success() {
    UpdateRecipeDTO partialUpdate =
        new UpdateRecipeDTO("Updated Title", null, null, "Updated Chef");

    when(recipeRepository.findByRecipeTitle("Test Recipe")).thenReturn(Optional.of(testRecipe));
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    Recipe result = recipeService.updateRecipe("Test Recipe", partialUpdate);

    assertNotNull(result);
    assertEquals("Updated Title", result.getRecipeTitle());
    assertEquals("Updated Chef", result.getChefName());
    assertNotNull(result.getIngredients());
    assertNotNull(result.getInstructions());
    verify(recipeRepository, times(1)).save(testRecipe);
  }

  @Test
  void updateRecipe_OnlyIngredients_Success() {
    List<String> newIngredients = Arrays.asList("newIng1", "newIng2", "newIng3");
    UpdateRecipeDTO ingredientsUpdate = new UpdateRecipeDTO(null, newIngredients, null, null);

    when(recipeRepository.findByRecipeTitle("Test Recipe")).thenReturn(Optional.of(testRecipe));
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    Recipe result = recipeService.updateRecipe("Test Recipe", ingredientsUpdate);

    assertNotNull(result);
    assertEquals(newIngredients, result.getIngredients());
    verify(recipeRepository, times(1)).save(testRecipe);
  }

  @Test
  void getRecipesByIngredient_EmptyResult_ThrowsException() {
    when(recipeRepository.findByRecipesByIngredient("nonexistent")).thenReturn(null);

    assertThrows(
        BussinessException.class, () -> recipeService.getRecipesByIngredient("nonexistent"));
  }

  @Test
  void getRecipesBySeason_EmptyResult_ThrowsException() {
    when(recipeRepository.findByRecipesBySeason("Winter")).thenReturn(null);

    assertThrows(BussinessException.class, () -> recipeService.getRecipesBySeason("Winter"));
  }

  @Test
  void createRecipe_DuplicateTitle_ThrowsException() {
    when(recipeRepository.findAll()).thenReturn(Arrays.asList(testRecipe));
    when(recipeRepository.save(any(Recipe.class))).thenThrow(new RuntimeException("Duplicate key"));

    assertThrows(BussinessException.class, () -> recipeService.createRecipe(testRecipeDTO));
  }

  @Test
  void deleteRecipe_RepositoryException_ThrowsBusinessException() {
    when(recipeRepository.findByRecipeTitle("Test Recipe")).thenReturn(Optional.of(testRecipe));
    doThrow(new RuntimeException("Database error")).when(recipeRepository).delete(testRecipe);

    assertThrows(BussinessException.class, () -> recipeService.deleteRecipe("Test Recipe"));
  }

  @Test
  void createRecipe_WithEmptyCollections_Success() {
    RecipeDTO emptyCollectionsDTO =
        new RecipeDTO(
            "Empty Collections Recipe",
            Arrays.asList(),
            Arrays.asList(),
            "Test Chef",
            null,
            ChefType.JURY);

    when(recipeRepository.findAll()).thenReturn(Arrays.asList());
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    Recipe result = recipeService.createRecipe(emptyCollectionsDTO);

    assertNotNull(result);
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }

  @Test
  void createRecipe_WithSpecialCharactersInTitle_Success() {
    RecipeDTO specialTitleDTO =
        new RecipeDTO(
            "Spécial Chäráctérs Récîpe! @#%",
            Arrays.asList("ing1"), Arrays.asList("step1"), "Test Chef", null, ChefType.VIEWER);

    when(recipeRepository.findAll()).thenReturn(Arrays.asList());
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    Recipe result = recipeService.createRecipe(specialTitleDTO);

    assertNotNull(result);
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }

  @Test
  void createRecipe_WithVeryLongStrings_Success() {
    String longString = "A".repeat(1000);
    List<String> longIngredients = Arrays.asList(longString, "B".repeat(500));
    List<String> longInstructions =
        Arrays.asList("Step 1: " + longString, "Step 2: " + "C".repeat(800));

    RecipeDTO longStringsDTO =
        new RecipeDTO(
            longString,
            longIngredients,
            longInstructions,
            "Chef " + longString,
            "Season " + longString,
            ChefType.PARTICIPANT);

    when(recipeRepository.findAll()).thenReturn(Arrays.asList());
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    Recipe result = recipeService.createRecipe(longStringsDTO);

    assertNotNull(result);
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }

  @Test
  void updateRecipe_WithEmptyUpdateDTO_NoChanges() {
    UpdateRecipeDTO emptyUpdateDTO = new UpdateRecipeDTO(null, null, null, null);

    when(recipeRepository.findByRecipeTitle("Test Recipe")).thenReturn(Optional.of(testRecipe));
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    Recipe result = recipeService.updateRecipe("Test Recipe", emptyUpdateDTO);

    assertNotNull(result);
    assertEquals("Test Recipe", result.getRecipeTitle());
    assertEquals(Arrays.asList("ing1", "ing2"), result.getIngredients());
    assertEquals(Arrays.asList("step1", "step2"), result.getInstructions());
    assertEquals("Test Chef", result.getChefName());
    verify(recipeRepository, times(1)).save(testRecipe);
  }

  @Test
  void createRecipe_WithDuplicateTitleInDatabase_ThrowsException() {
    when(recipeRepository.findAll()).thenReturn(Arrays.asList(testRecipe));
    when(recipeRepository.save(any(Recipe.class)))
        .thenThrow(new org.springframework.dao.DuplicateKeyException("Duplicate title"));

    assertThrows(BussinessException.class, () -> recipeService.createRecipe(testRecipeDTO));
  }

  @Test
  void getRecipesByIngredient_CaseInsensitive_Success() {
    when(recipeRepository.findByRecipesByIngredient("ING1")).thenReturn(Arrays.asList(testRecipe));

    List<Recipe> result = recipeService.getRecipesByIngredient("ING1");

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void createRecipe_WithNullChefName_Success() {
    RecipeDTO nullChefNameDTO =
        new RecipeDTO(
            "Test Recipe",
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            null,
            null,
            ChefType.JURY);

    when(recipeRepository.findAll()).thenReturn(Arrays.asList());
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    Recipe result = recipeService.createRecipe(nullChefNameDTO);

    assertNotNull(result);
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }

  @Test
  void updateRecipe_ChangeChefType_NotAllowed() {
    when(recipeRepository.findByRecipeTitle("Test Recipe")).thenReturn(Optional.of(testRecipe));
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    ChefType originalChefType = testRecipe.getChefType();

    Recipe result = recipeService.updateRecipe("Test Recipe", testUpdateDTO);

    assertEquals(originalChefType, result.getChefType());
  }

  @Test
  void createRecipe_IDGeneration_Sequential() {
    when(recipeRepository.findAll()).thenReturn(Arrays.asList(testRecipe));
    when(recipeRepository.save(any(Recipe.class)))
        .thenAnswer(
            invocation -> {
              Recipe savedRecipe = invocation.getArgument(0);
              return savedRecipe;
            });

    Recipe result = recipeService.createRecipe(testRecipeDTO);

    assertEquals("2", result.getId());
  }

  @Test
  void getRecipeById_WithSpecialCharacters() {
    String specialId = "123-abc_456@special";
    when(recipeRepository.findByRecipeById(specialId)).thenReturn(Optional.of(testRecipe));

    Recipe result = recipeService.getRecipeById(specialId);

    assertNotNull(result);
    verify(recipeRepository, times(1)).findByRecipeById(specialId);
  }

  @Test
  void createRecipe_WithWhitespaceOnlyStrings_ThrowsException() {
    RecipeDTO whitespaceDTO =
        new RecipeDTO(
            "   ", Arrays.asList("ing1"), Arrays.asList("step1"), "Chef", null, ChefType.JURY);

    assertThrows(BussinessException.class, () -> recipeService.createRecipe(whitespaceDTO));
  }

  @Test
  void createRecipe_ParticipantWithoutSeason_ThrowsException() {
    RecipeDTO participantNoSeasonDTO =
        new RecipeDTO(
            "Participant Recipe",
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            "Participant Chef",
            null,
            ChefType.PARTICIPANT);

    assertThrows(
        BussinessException.class, () -> recipeService.createRecipe(participantNoSeasonDTO));
  }

  @Test
  void createRecipe_ParticipantWithEmptySeason_ThrowsException() {
    RecipeDTO participantEmptySeasonDTO =
        new RecipeDTO(
            "Participant Recipe",
            Arrays.asList("ing1", "ing2"),
            Arrays.asList("step1", "step2"),
            "Participant Chef",
            "",
            ChefType.PARTICIPANT);

    assertThrows(
        BussinessException.class, () -> recipeService.createRecipe(participantEmptySeasonDTO));
  }

  @Test
  void updateRecipe_WithEmptyStrings_HandledCorrectly() {
    UpdateRecipeDTO emptyStringsDTO = new UpdateRecipeDTO("", Arrays.asList(), Arrays.asList(), "");

    when(recipeRepository.findByRecipeTitle("Test Recipe")).thenReturn(Optional.of(testRecipe));
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    Recipe result = recipeService.updateRecipe("Test Recipe", emptyStringsDTO);

    assertNotNull(result);
    assertEquals("", result.getRecipeTitle());
    assertEquals(Arrays.asList(), result.getIngredients());
    assertEquals(Arrays.asList(), result.getInstructions());
    assertEquals("", result.getChefName());
  }
}
