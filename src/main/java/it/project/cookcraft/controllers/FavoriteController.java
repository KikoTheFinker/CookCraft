package it.project.cookcraft.controllers;

import it.project.cookcraft.models.User;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FavoriteController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public FavoriteController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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

        userService.addRecipeToFavoritesById(user.get().getId(), recipeId);
        return new ResponseEntity<>("Successfully added recipe to favorites.", HttpStatus.OK);
    }
}