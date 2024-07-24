package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<Recipe> findAllRecipes();
    Optional<Recipe> findRecipeById(Long id);
    void saveRecipe(Recipe recipe);
    void updateRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
}
