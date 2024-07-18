package it.project.cookcraft.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "recipe")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {
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

}
