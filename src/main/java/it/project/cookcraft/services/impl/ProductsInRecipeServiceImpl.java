package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.ProductsInRecipeDAO;
import it.project.cookcraft.models.ProductsInRecipe;
import it.project.cookcraft.services.interfaces.ProductsInRecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsInRecipeServiceImpl implements ProductsInRecipeService {


    private final ProductsInRecipeDAO productsInRecipeDAO;

    public ProductsInRecipeServiceImpl(ProductsInRecipeDAO productsInRecipeDAO) {
        this.productsInRecipeDAO = productsInRecipeDAO;
    }

    @Override
    public List<ProductsInRecipe> findAllProductsInRecipe() {
        return productsInRecipeDAO.findAll();
    }

    @Override
    public Optional<ProductsInRecipe> findProductsInRecipeById(int id) {
        return productsInRecipeDAO.findById(id);
    }

    @Override
    public void saveProductsInRecipe(ProductsInRecipe productsInRecipe) {
        productsInRecipeDAO.save(productsInRecipe);
    }

    @Override
    public void updateProductsInRecipe(ProductsInRecipe productsInRecipe) {
        productsInRecipeDAO.update(productsInRecipe);
    }

    @Override
    public void deleteProductsInRecipe(ProductsInRecipe productsInRecipe) {
        productsInRecipeDAO.delete(productsInRecipe);
    }
}
