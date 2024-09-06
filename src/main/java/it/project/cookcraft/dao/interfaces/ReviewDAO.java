package it.project.cookcraft.dao.interfaces;
import it.project.cookcraft.models.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDAO {
    List<Review> findAll();
    Optional<Review> findById(Long id);
    List<Review> findByRecipeId(Long recipeId);
    List<Review> findByUserId(Long userId);
    void save(Review review);
    void update(Review review);
    void delete(Long id);
}
