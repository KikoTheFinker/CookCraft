package it.project.cookcraft.dto;

import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.models.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeWithProductsDTO {
    private Recipe recipe;
    private List<ProductDTO> productsInRecipes;
    private List<Review> reviews;
}
