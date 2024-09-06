package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.ReviewDAO;
import it.project.cookcraft.models.Review;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@Repository
public class ReviewDAOImpl implements ReviewDAO {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class ReviewMapper implements RowMapper<Review> {
        @Override
        public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
            Review review = new Review();
            review.setId(rs.getLong("id"));
            review.setRating(rs.getDouble("rating"));
            review.setReview(rs.getString("review"));
            review.setUserId(rs.getLong("user_id"));
            review.setRecipeId(rs.getLong("recipe_id"));
            review.setUserName(rs.getString("user_name"));
            review.setUserSurname(rs.getString("user_surname"));
            review.setMealThumb(rs.getString("meal_thumb"));
            review.setRecipeName(rs.getString("recipe_name"));
            return review;
        }
    }

    @Override
    public List<Review> findAll() {
        String sql = "SELECT r.id, r.rating, r.review, r.user_id, r.recipe_id, u.user_name, u.user_surname, re.meal_thumb,  re.recipe_name " +
                "FROM review r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN recipe re ON r.recipe_id = re.id";
        return jdbcTemplate.query(sql, new ReviewMapper());
    }

    @Override
    public Optional<Review> findById(Long id) {
        String sql = "SELECT r.id, r.rating, r.review, r.user_id, r.recipe_id, u.user_name, u.user_surname, re.meal_thumb, re.recipe_name " +
                "FROM review r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN recipe re ON r.recipe_id = re.id " +
                "WHERE r.id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new ReviewMapper())
                .stream().findFirst();
    }

    @Override
    public List<Review> findByUserId(Long userId) {
        String sql = "SELECT r.id, r.rating, r.review, r.user_id, r.recipe_id, u.user_name, u.user_surname, re.meal_thumb, re.recipe_name " +
                "FROM review r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN recipe re ON r.recipe_id = re.id " +
                "WHERE r.user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new ReviewMapper());
    }

    @Override
    public List<Review> findByRecipeId(Long recipeId) {
        String sql = "SELECT r.id, r.rating, r.review, r.user_id, r.recipe_id, u.user_name, u.user_surname, re.meal_thumb,  re.recipe_name " +
                "FROM review r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN recipe re ON r.recipe_id = re.id " +
                "WHERE r.recipe_id = ?";
        return jdbcTemplate.query(sql, new Object[]{recipeId}, new ReviewMapper());
    }

    @Override
    public void save(Review review) {
        jdbcTemplate.update("INSERT INTO review (rating, review, user_id, recipe_id) VALUES (?, ?, ?, ?)",
                review.getRating(), review.getReview(), review.getUserId(), review.getRecipeId());
    }

    @Override
    public void update(Review review) {
        jdbcTemplate.update("UPDATE review SET rating = ?, review = ?, user_id = ?, recipe_id = ? WHERE id = ?",
                review.getRating(), review.getReview(), review.getUserId(), review.getRecipeId(), review.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM review WHERE id = ?", id);
    }
}
