package it.project.cookcraft.inserters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsInserter {
    @JsonProperty("strIngredient1")
    private String ingredient1;
    @JsonProperty("strIngredient2")
    private String ingredient2;
    @JsonProperty("strIngredient3")
    private String ingredient3;
    @JsonProperty("strIngredient4")
    private String ingredient4;
    @JsonProperty("strIngredient5")
    private String ingredient5;
    @JsonProperty("strIngredient6")
    private String ingredient6;
    @JsonProperty("strIngredient7")
    private String ingredient7;
    @JsonProperty("strIngredient8")
    private String ingredient8;
    @JsonProperty("strIngredient9")
    private String ingredient9;
    @JsonProperty("strIngredient10")
    private String ingredient10;
    @JsonProperty("strIngredient11")
    private String ingredient11;
    @JsonProperty("strIngredient12")
    private String ingredient12;
    @JsonProperty("strIngredient13")
    private String ingredient13;
    @JsonProperty("strIngredient14")
    private String ingredient14;
    @JsonProperty("strIngredient15")
    private String ingredient15;
    @JsonProperty("strIngredient16")
    private String ingredient16;
    @JsonProperty("strIngredient17")
    private String ingredient17;
    @JsonProperty("strIngredient18")
    private String ingredient18;
    @JsonProperty("strIngredient19")
    private String ingredient19;
    @JsonProperty("strIngredient20")
    private String ingredient20;
}
