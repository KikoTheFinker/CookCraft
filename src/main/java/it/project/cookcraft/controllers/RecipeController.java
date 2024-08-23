package it.project.cookcraft.controllers;

import it.project.cookcraft.dto.ProductDTO;
import it.project.cookcraft.dto.RecipeWithProductsDTO;
import it.project.cookcraft.models.ProductsInRecipe;
import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.services.interfaces.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public ResponseEntity<Page<Recipe>> getRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String nationality
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipes;

        if (StringUtils.hasText(nationality) && StringUtils.hasText(category))
        {
            recipes = recipeService.findRecipesByNationalityAndCategory(nationality, category, pageable);
        }
        else if (StringUtils.hasText(nationality)) {
            recipes = recipeService.findRecipesByNationality(nationality, pageable);
        } else if (StringUtils.hasText(category)) {
            recipes = recipeService.findRecipesByCategory(category, pageable);
        } else {
            recipes = recipeService.findAllRecipes(pageable);
        }

        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeWithProductsDTO> getRecipe(@PathVariable Long id) {
        RecipeWithProductsDTO recipeWithProductsDTO = new RecipeWithProductsDTO();
        Optional<Recipe> recipe = recipeService.findRecipeById(id);

        if(recipe.isPresent())
        {
            List<ProductDTO> productList = recipeService.findProductsByRecipeId(id);
            recipeWithProductsDTO.setRecipe(recipe.get());
            recipeWithProductsDTO.setProductsInRecipes(productList);
            return new ResponseEntity<>(recipeWithProductsDTO, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
