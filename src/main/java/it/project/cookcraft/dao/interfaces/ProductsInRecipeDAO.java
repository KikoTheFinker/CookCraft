package it.project.cookcraft.dao.interfaces;

import it.project.cookcraft.models.ProductsInRecipe;

import java.util.List;
import java.util.Optional;

public interface ProductsInRecipeDAO {
    List<ProductsInRecipe> findAll();
    Optional<ProductsInRecipe> findById(int id);
    void save(ProductsInRecipe productsInRecipe);
    void update(ProductsInRecipe productsInRecipe);
    void delete(ProductsInRecipe productsInRecipe);
}
