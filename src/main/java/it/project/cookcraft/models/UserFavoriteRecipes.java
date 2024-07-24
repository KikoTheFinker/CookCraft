package it.project.cookcraft.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoriteRecipes {
    private Long id;
    private Long userId;
    private List<Long> RecipeIds;
}
