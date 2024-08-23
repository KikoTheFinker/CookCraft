package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.dto.ProductDTO;
import it.project.cookcraft.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<Recipe> findAllRecipes();
    Page<Recipe> findAllRecipes(Pageable pageable);
    Optional<Recipe> findRecipeById(Long id);
    void saveRecipe(Recipe recipe);
    void updateRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
    Page<Recipe> findRecipesByNationality(String nationality, Pageable pageable);
    Page<Recipe> findRecipesByCategory(String category, Pageable pageable);
    Page<Recipe> findRecipesByNationalityAndCategory(String nationality, String category, Pageable pageable);
    List<ProductDTO> findProductsByRecipeId(Long id);
}
