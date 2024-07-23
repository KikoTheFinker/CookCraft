package it.project.cookcraft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {
    private Long id;

    @JsonProperty("strMeal")
    private String name;

    @JsonProperty("strInstructions")
    private String description;

    @JsonProperty("strCategory")
    private String category;

    @JsonProperty("strArea")
    private String origin;

    @JsonProperty("strMealThumb")
    private String mealThumb;

    @JsonProperty("strYoutube")
    private String videoUrl;

    private List<ProductsInRecipe> products;
}
