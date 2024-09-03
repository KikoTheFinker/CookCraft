package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Product;
import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.services.interfaces.ProductService;
import it.project.cookcraft.services.interfaces.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductsController {

    private final ProductService productService;
    private final RecipeService recipeService;

    public ProductsController(ProductService productService, RecipeService recipeService) {
        this.productService = productService;
        this.recipeService = recipeService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Recipe>> searchRecipesByFilters(
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false) String category,
            @RequestBody(required = false) List<Long> productIds,
            Pageable pageable
    ) {
        Page<Recipe> recipesPage = recipeService.findRecipesByFilters(nationality, category, productIds, pageable);
        return ResponseEntity.ok(recipesPage.getContent());
    }

}
