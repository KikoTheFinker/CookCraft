/*
package it.project.cookcraft.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_favorite_recipes")
@Data
@NoArgsConstructor
public class UserFavoriteRecipes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "user_id")
    User user;

    @OneToOne(mappedBy = "recipe_id")
    Recipe recipe;
}
*/
