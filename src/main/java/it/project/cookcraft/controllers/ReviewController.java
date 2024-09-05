package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Review;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.ReviewService;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, JwtUtil jwtUtil, UserService userService) {
        this.reviewService = reviewService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> addReview(@RequestBody Review review, @RequestHeader("Authorization") String token) {
        try {
            // Add review logic
            String jwtToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(jwtToken);
            Long userId = userService.findUserByEmail(userEmail).get().getId();
            review.setUserId(userId);

            reviewService.addReview(review);

            List<Review> updatedReviews = reviewService.getReviewsByRecipeId(review.getRecipeId());
            return new ResponseEntity<>(updatedReviews, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add review", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
