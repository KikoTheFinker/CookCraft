package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.RecipeDAO;
import it.project.cookcraft.dto.ProductDTO;
import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.services.interfaces.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeDAO recipeDAO;

    public RecipeServiceImpl(RecipeDAO recipeDAO) {
        this.recipeDAO = recipeDAO;
    }

    @Override
    public Page<Recipe> findAllRecipes(Pageable pageable) {
        return recipeDAO.findAll(pageable);
    }

    @Override
    public Optional<Recipe> findRecipeById(Long id) {
        return recipeDAO.findById(id);
    }

    @Override
    public void saveRecipe(Recipe recipe) {
        recipeDAO.save(recipe);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        recipeDAO.update(recipe);
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        recipeDAO.delete(recipe);
    }


    @Override
    public List<ProductDTO> findProductsByRecipeId(Long recipeId) {
        return recipeDAO.findProductsByRecipeId(recipeId);
    }

    @Override
    public Page<Recipe> findRecipesByFilters(String nationality, String category, List<Long> productIds, String searchTerm, Pageable pageable) {
        return recipeDAO.findRecipesByFilters(nationality, category, productIds, searchTerm, pageable);
    }
}
