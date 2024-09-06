package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getAllReviews();
    Optional<Review> getReviewById(Long id);
    List<Review> getReviewsByRecipeId(Long recipeId);
    List<Review> getReviewsByUserId(Long userId);
    void addReview(Review review);
    void updateReview(Review review);
    void deleteReview(Long id);
}
