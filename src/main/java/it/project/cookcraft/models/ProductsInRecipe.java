package it.project.cookcraft.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsInRecipe {
    private Long id;
    private List<Long> productIds;
    private Long recipeId;
    private List<String> measurement;
}
