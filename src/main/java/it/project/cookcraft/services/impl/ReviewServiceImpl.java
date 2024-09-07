package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.ReviewDAO;
import it.project.cookcraft.models.Review;
import it.project.cookcraft.services.interfaces.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewServiceImpl(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewDAO.findAll();
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        return reviewDAO.findById(id);
    }

    @Override
    public List<Review> getReviewsByRecipeId(Long recipeId) {
        return reviewDAO.findByRecipeId(recipeId);
    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewDAO.findByUserId(userId);
    }

    @Override
    public void addReview(Review review) {
        reviewDAO.save(review);
    }

    @Override
    public void updateReview(Review review) {
        reviewDAO.update(review);
    }

    @Override
    public boolean deleteReview(Long id, String userEmail) {
        return reviewDAO.delete(id, userEmail);
    }

    @Override
    public boolean hasUserReviewedRecipe(Long userId, Long recipeId) {
        return reviewDAO.hasUserReviewedRecipe(userId, recipeId);
    }
}
