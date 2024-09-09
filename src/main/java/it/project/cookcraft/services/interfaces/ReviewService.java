package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getAllReviews();
    Optional<Review> getReviewById(Long id);
    List<Review> getReviewsByRecipeId(Long recipeId);
    List<Review> getReviewsByUserId(Long userId);
    void addReview(Review review);
    void updateReview(Review review);
    boolean deleteReview(Long id, String userEmail);
    boolean hasUserReviewedRecipe(Long userId, Long recipeId);
    Page<Review> findAllReviews(Pageable pageable);
    boolean deleteReviewByIdViaAdmin(Long id);
}
