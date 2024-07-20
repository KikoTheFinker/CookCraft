/*
package it.project.cookcraft.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Entity
@Data
@Table(name = "products_in_recipe")
@AllArgsConstructor
@NoArgsConstructor
public class ProductsInRecipe {
    @EmbeddedId
    private ProductsInRecipeKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private String measure;
}

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
class ProductsInRecipeKey implements Serializable {
    @Column(name = "product_id")
    private int productId;

    @Column(name = "recipe_id")
    private int recipeId;
}
*/
