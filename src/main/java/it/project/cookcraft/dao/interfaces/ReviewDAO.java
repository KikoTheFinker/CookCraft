package it.project.cookcraft.dao.interfaces;
import it.project.cookcraft.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewDAO {
    List<Review> findAll();
    Optional<Review> findById(Long id);
    List<Review> findByRecipeId(Long recipeId);
    List<Review> findByUserId(Long userId);
    void save(Review review);
    void update(Review review);
    boolean delete(Long id, String userEmail);
    boolean hasUserReviewedRecipe(Long userId, Long recipeId);
    Page<Review> findAllReviews(Pageable pageable);
}
