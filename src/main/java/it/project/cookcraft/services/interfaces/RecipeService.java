package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.dto.ProductDTO;
import it.project.cookcraft.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Page<Recipe> findAllRecipes(Pageable pageable);
    Optional<Recipe> findRecipeById(Long id);
    void saveRecipe(Recipe recipe);
    void updateRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
    List<ProductDTO> findProductsByRecipeId(Long id);
    Page<Recipe> findRecipesByFilters(String nationality, String category, List<Long> productIds, String searchTerm, Pageable pageable);
}
