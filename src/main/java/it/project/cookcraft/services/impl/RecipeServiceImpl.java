package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.RecipeDAO;
import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.services.interfaces.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeDAO recipeDAO;

    @Override
    public List<Recipe> findAllRecipes() {
        return recipeDAO.findAll();
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
}
