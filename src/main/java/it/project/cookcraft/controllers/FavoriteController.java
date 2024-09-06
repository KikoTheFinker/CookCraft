package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.models.User;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.RecipeService;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FavoriteController {

    private final UserService userService;
    private final RecipeService recipeService;
    private final JwtUtil jwtUtil;

    public FavoriteController(UserService userService, RecipeService recipeService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.recipeService = recipeService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/favorite/check")
    public ResponseEntity<Boolean> isFavorite(@RequestParam String userEmail, @RequestParam Long recipeId) {
        Optional<User> user = userService.findUserByEmail(userEmail);
        if(user.isPresent())
        {
            Long userId = user.get().getId();
            boolean isLiked = userService.alreadyFavorited(userId, recipeId);
            return new ResponseEntity<>(isLiked, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @GetMapping("/favorite/user")
    public ResponseEntity<?> getFavoriteRecipes(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtUtil.extractEmail(jwtToken);

            Optional<User> user = userService.findUserByEmail(email);
            if(user.isPresent())
            {
                Long userId = user.get().getId();
                List<Recipe> recipes = recipeService.getUserFavoriteRecipesById(userId);
                return new ResponseEntity<>(recipes, HttpStatus.OK);
            }
            else return new ResponseEntity<>("Authentication token most likely expired.", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>("Error fetching recipes.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/favorite")
    public ResponseEntity<String> addFavoriteRecipe(@RequestBody Map<String, String> request) {
        String userEmail = request.get("userEmail");
        Long recipeId = Long.valueOf(request.get("recipeId"));
        Optional<User> user = userService.findUserByEmail(userEmail);

        if(user.isEmpty())
        {
            return new ResponseEntity<>("Error processing user email.", HttpStatus.NOT_FOUND);
        }

        if(userService.alreadyFavorited(user.get().getId(), recipeId))
        {
            userService.removeRecipeFromFavoriteById(user.get().getId(), recipeId);
            return new ResponseEntity<>("Removed recipe from favorites.", HttpStatus.OK);
        }

        userService.addRecipeToFavoritesById(user.get().getId(), recipeId);
        return new ResponseEntity<>("Successfully added recipe to favorites.", HttpStatus.OK);
    }
}