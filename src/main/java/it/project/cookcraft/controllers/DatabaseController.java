package it.project.cookcraft.controllers;

import it.project.cookcraft.models.ProductsInRecipe;
import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.models.User;
import it.project.cookcraft.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/users")
    public List<User> getData() {
        return databaseService.getAllUsers();
    }
    @GetMapping("/recipes")
    public List<Recipe> getRecipes() {return databaseService.getAllRecipes();}
    @GetMapping("/products/in/recipe")
    public List<ProductsInRecipe> getProductsInRecipe(){return databaseService.getAllProductsInRecipe();}
}
