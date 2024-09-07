package it.project.cookcraft.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Long id;
    private double rating;
    private String review;
    private Long userId;
    private Long recipeId;
    private String userName;
    private String userSurname;
    private String mealThumb;
    private String recipeName;
    private String userEmail;
}
