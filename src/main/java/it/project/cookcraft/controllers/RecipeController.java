package it.project.cookcraft.controllers;

import it.project.cookcraft.dto.ProductDTO;
import it.project.cookcraft.dto.RecipeWithProductsDTO;
import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.models.Review;
import it.project.cookcraft.services.interfaces.RecipeService;
import it.project.cookcraft.services.interfaces.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RecipeController {
    private final RecipeService recipeService;
    private final ReviewService reviewService;

    public RecipeController(RecipeService recipeService, ReviewService reviewService) {
        this.recipeService = recipeService;
        this.reviewService = reviewService;
    }

    @GetMapping("/recipes")
    public ResponseEntity<Page<Recipe>> getRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false) List<Long> productId,
            @RequestParam(required = false) String searchTerm
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes = recipeService.findRecipesByFilters(nationality, category, productId, searchTerm, pageable);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }



    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeWithProductsDTO> getRecipe(@PathVariable Long id) {
        RecipeWithProductsDTO recipeWithProductsDTO = new RecipeWithProductsDTO();
        Optional<Recipe> recipe = recipeService.findRecipeById(id);
        if(recipe.isPresent())
        {
            List<Review> reviews = reviewService.getReviewsByRecipeId(id);
            List<ProductDTO> productList = recipeService.findProductsByRecipeId(id);
            recipeWithProductsDTO.setRecipe(recipe.get());
            recipeWithProductsDTO.setProductsInRecipes(productList);
            recipeWithProductsDTO.setReviews(reviews);
            return new ResponseEntity<>(recipeWithProductsDTO, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
