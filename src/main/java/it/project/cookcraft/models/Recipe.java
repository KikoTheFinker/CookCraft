package it.project.cookcraft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;


@Data
@Table(name = "recipe")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("strMeal")
    @Column(name = "recipe_name")
    private String name;

    @JsonProperty("strInstructions")
    @Column(name = "description")
    private String description;

    @JsonProperty("strCategory")
    @Column(name = "category")
    private String category;

    @JsonProperty("strArea")
    @Column(name = "origin")
    private String origin;

    @JsonProperty("strMealThumb")
    @Column(name = "meal_thumb")
    private String mealThumb;

    @JsonProperty("strYoutube")
    @Column(name = "video_url")
    private String videoUrl;

    @OneToMany(mappedBy = "recipe")
    private List<ProductsInRecipe> products;
}
