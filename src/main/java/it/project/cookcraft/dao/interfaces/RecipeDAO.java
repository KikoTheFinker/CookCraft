package it.project.cookcraft.dao.interfaces;

import it.project.cookcraft.dto.ProductDTO;
import it.project.cookcraft.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecipeDAO {
    List<Recipe> findAll();
    Page<Recipe> findAll(Pageable pageable);
    Optional<Recipe> findById(Long id);
    void save(Recipe recipe);
    void update(Recipe recipe);
    void delete(Recipe recipe);
    List<ProductDTO> findProductsByRecipeId(Long recipeId);
    Page<Recipe> findRecipesByFilters(String nationality, String category, List<Long> productIds, String searchTerm, Pageable pageable);
}
