package it.project.cookcraft.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;


@Data
@NoArgsConstructor
public class ProductsInRecipe {
    private Long id;

    private Product product;

    private Recipe recipe;

    private String measurement;
}
