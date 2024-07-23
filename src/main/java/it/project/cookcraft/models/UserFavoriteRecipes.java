package it.project.cookcraft.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoriteRecipes {
    private Long id;
    private User user;
    private Recipe recipe;
}
