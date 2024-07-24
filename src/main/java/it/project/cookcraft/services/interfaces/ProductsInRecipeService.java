package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.ProductsInRecipe;

import java.util.List;
import java.util.Optional;

public interface ProductsInRecipeService {
    List<ProductsInRecipe> findAllProductsInRecipe();
    Optional<ProductsInRecipe> findProductsInRecipeById(int id);
    void saveProductsInRecipe(ProductsInRecipe productsInRecipe);
    void updateProductsInRecipe(ProductsInRecipe productsInRecipe);
    void deleteProductsInRecipe(ProductsInRecipe productsInRecipe);
}
