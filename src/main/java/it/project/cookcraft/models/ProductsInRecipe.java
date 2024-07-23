package it.project.cookcraft.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsInRecipe {
    private Long id;
    private Product product;
    private Recipe recipe;
    private String measurement;
}
