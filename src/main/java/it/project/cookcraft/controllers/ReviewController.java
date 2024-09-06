package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.models.Review;
import it.project.cookcraft.models.User;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.RecipeService;
import it.project.cookcraft.services.interfaces.ReviewService;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RecipeService recipeService;

    public ReviewController(ReviewService reviewService, JwtUtil jwtUtil, UserService userService, RecipeService recipeService) {
        this.reviewService = reviewService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> addReview(@RequestBody Review review, @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(jwtToken);
            if (userService.findUserByEmail(userEmail).isPresent()) {
                Long userId = userService.findUserByEmail(userEmail).get().getId();
                review.setUserId(userId);
            }

            Optional<Recipe> recipe = recipeService.findRecipeById(review.getRecipeId());
            if (recipe.isPresent()) {
                review.setMealThumb(recipe.get().getMealThumb());
                reviewService.addReview(review);
                List<Review> updatedReviews = reviewService.getReviewsByRecipeId(review.getRecipeId());
                return new ResponseEntity<>(updatedReviews, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Recipe not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add review", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/reviews/user")
    public ResponseEntity<?> getReviewsByUser(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(jwtToken);

            Optional<User> userOptional = userService.findUserByEmail(userEmail);
            if (userOptional.isPresent()) {
                Long userId = userOptional.get().getId();
                List<Review> userReviews = reviewService.getReviewsByUserId(userId);
                return new ResponseEntity<>(userReviews, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to fetch reviews", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
