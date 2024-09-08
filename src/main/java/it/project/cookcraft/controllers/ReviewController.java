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

    @GetMapping("/reviews")
    public ResponseEntity<?> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        if(reviews != null)
        {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> addReview(@RequestBody Review review, @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(jwtToken);
            Optional<User> userOptional = userService.findUserByEmail(userEmail);

            if (userOptional.isPresent()) {
                Long userId = userOptional.get().getId();
                review.setUserId(userId);

                if (reviewService.hasUserReviewedRecipe(userId, review.getRecipeId())) {
                    return new ResponseEntity<>("You have already reviewed this recipe", HttpStatus.BAD_REQUEST);
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
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add review", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/reviews/check")
    public ResponseEntity<?> hasReviewed(@RequestParam String userEmail, @RequestParam Long recipeId) {
        Optional<User> userOptional = userService.findUserByEmail(userEmail);

        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();
            boolean hasReviewed = reviewService.hasUserReviewedRecipe(userId, recipeId);
            return ResponseEntity.ok(hasReviewed);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
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
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(jwtToken);

            boolean isDeleted = reviewService.deleteReview(reviewId, userEmail);

            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized or review not found", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete review", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
