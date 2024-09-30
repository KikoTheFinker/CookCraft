package it.project.cookcraft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeApplicationDTO {
    private Long Id;
    private String recipeName;
    private String recipeDesc;
    private String recipeCategory;
    private String recipeOrigin;
    private String recipeVideoURL;
    private String recipeMealThumb;
    private List<IngredientsDTO> ingredients;
}
