package it.project.cookcraft.services;

import it.project.cookcraft.dao.impls.ProductsInRecipeDAOImpl;
import it.project.cookcraft.dao.impls.RecipeDAOImpl;
import it.project.cookcraft.dao.impls.UserDAOimpl;
import it.project.cookcraft.models.ProductsInRecipe;
import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> getAllUsers() {
        UserDAOimpl userDAO = new UserDAOimpl(jdbcTemplate);
        return userDAO.findAll();
    }

    public List<Recipe> getAllRecipes() {
        RecipeDAOImpl recipeDAO = new RecipeDAOImpl(jdbcTemplate);
        return recipeDAO.findAll();
    }

    public List<ProductsInRecipe> getAllProductsInRecipe() {
        ProductsInRecipeDAOImpl productsInRecipeDAO = new ProductsInRecipeDAOImpl(jdbcTemplate);
        return productsInRecipeDAO.findAll();
    }
}
