package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.DeliveryPersonDAO;
import it.project.cookcraft.dao.interfaces.ReviewDAO;
import it.project.cookcraft.models.DeliveryPerson;
import it.project.cookcraft.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
            review.setUserEmail(rs.getString("userEmail"));
            return review;
        }
    }

    @Override
    public List<Review> findAll() {
        String sql = "SELECT r.id, r.rating, r.review, r.user_id, r.recipe_id, u.user_name, u.user_surname, re.meal_thumb,  re.recipe_name, u.email AS userEmail " +
                "FROM review r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN recipe re ON r.recipe_id = re.id";
        return jdbcTemplate.query(sql, new ReviewMapper());
    }
    @Override
    public Optional<Review> findById(Long id) {
        String sql = "SELECT r.id, r.rating, r.review, r.user_id, r.recipe_id, u.user_name, u.user_surname, u.email AS userEmail, re.meal_thumb, re.recipe_name " +
                "FROM review r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN recipe re ON r.recipe_id = re.id " +
                "WHERE r.id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new ReviewMapper())
                .stream().findFirst();
    }

    @Override
    public List<Review> findByUserId(Long userId) {
        String sql = "SELECT r.id, r.rating, r.review, r.user_id, r.recipe_id, u.user_name, u.user_surname, u.email AS userEmail, re.meal_thumb, re.recipe_name " +
                "FROM review r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN recipe re ON r.recipe_id = re.id " +
                "WHERE r.user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new ReviewMapper());
    }


    @Override
    public List<Review> findByRecipeId(Long recipeId) {
        String sql = "SELECT r.id, r.rating, r.review, r.user_id, r.recipe_id, u.user_name, u.user_surname, u.email AS userEmail, re.meal_thumb, re.recipe_name " +
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
    public boolean delete(Long id, String userEmail) {
        String sql = "DELETE FROM review WHERE id = ? AND user_id = (SELECT id FROM users WHERE email = ?)";
        int rowsAffected = jdbcTemplate.update(sql, id, userEmail);
        return rowsAffected > 0;
    }


    @Override
    public boolean hasUserReviewedRecipe(Long userId, Long recipeId) {
        String sql = "SELECT COUNT(*) FROM review WHERE user_id = ? AND recipe_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, recipeId}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public Page<Review> findAllReviews(Pageable pageable) {
        String sql = "SELECT r.id, r.rating, r.review, r.user_id, r.recipe_id, u.user_name, u.user_surname, u.email AS userEmail, re.meal_thumb, re.recipe_name " +
                "FROM review r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN recipe re ON r.recipe_id = re.id " +
                "ORDER BY r.id " +
                "LIMIT ? OFFSET ?";

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM review", Integer.class);

        List<Review> reviews = jdbcTemplate.query(sql, new Object[]{pageable.getPageSize(), pageable.getOffset()}, new ReviewMapper());

        return new PageImpl<>(reviews, pageable, count != null ? count : 0);
    }

    @Override
    public boolean deleteReviewByIdViaAdmin(Long reviewId) {
        String sql = "DELETE FROM review WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, reviewId);
        return rowsAffected > 0;
    }


    @Repository
    public static class DeliveryPersonDAOImpl implements DeliveryPersonDAO {

        private final JdbcTemplate jdbcTemplate;

        public DeliveryPersonDAOImpl(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }
        private static final class DeliveryPersonRowMapper implements RowMapper<DeliveryPerson> {
            @Override
            public DeliveryPerson mapRow(ResultSet rs, int rowNum) throws SQLException {
                DeliveryPerson deliveryPerson = new DeliveryPerson();
                deliveryPerson.setId(rs.getLong("id"));
                deliveryPerson.setActive(rs.getBoolean("active"));
                deliveryPerson.setTotalDistance(rs.getDouble("total_distance"));
                deliveryPerson.setUserId(rs.getLong("user_id"));
                deliveryPerson.setProductOrderId(rs.getLong("product_order_id"));
                return deliveryPerson;
            }
        }

        @Override
        public Optional<DeliveryPerson> findDeliveryPersonByUserId(Long userId) {
            String sql = "SELECT * FROM delivery_person WHERE user_id = ?";
            return jdbcTemplate.query(sql, new Object[]{userId}, new DeliveryPersonRowMapper()).stream().findFirst();
        }

        @Override
        public void save(DeliveryPerson deliveryPerson) {
            String sql = "INSERT INTO delivery_person (active, total_distance, user_id, product_order_id) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    deliveryPerson.isActive(),
                    deliveryPerson.getTotalDistance(),
                    deliveryPerson.getUserId(),
                    deliveryPerson.getProductOrderId());
        }

        @Override
        public Optional<DeliveryPerson> findById(Long id) {
            String sql = "SELECT * FROM delivery_person WHERE id = ?";
            return jdbcTemplate.query(sql, new Object[]{id}, new DeliveryPersonRowMapper()).stream().findFirst();
        }

        @Override
        public Optional<DeliveryPerson> findByUserId(Long userId) {
            return findDeliveryPersonByUserId(userId); // Reuse the method
        }

        @Override
        public void update(DeliveryPerson deliveryPerson) {
            String sql = "UPDATE delivery_person SET active = ?, total_distance = ?, product_order_id = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    deliveryPerson.isActive(),
                    deliveryPerson.getTotalDistance(),
                    deliveryPerson.getProductOrderId(),
                    deliveryPerson.getId());
        }
    }
}
