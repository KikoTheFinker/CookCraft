package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.UserDAO;
import it.project.cookcraft.dto.UserDTO;
import it.project.cookcraft.models.User;
import it.project.cookcraft.models.UserType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOimpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    public UserDAOimpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("user_name"));
            user.setSurname(rs.getString("user_surname"));
            user.setEmail(rs.getString("email"));
            user.setAddress(rs.getString("address"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setPassword(rs.getString("password"));

            String userTypeString = rs.getString("user_type");
            UserType userType = UserType.valueOf(userTypeString);
            user.setUserType(userType);

            return user;
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
    }

    @Override
    public Optional<User> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new Object[]{id}, new UserRowMapper()).stream().findFirst();
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update("INSERT INTO users (user_name, user_surname, email, address ,phone_number, password , user_type) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?::usertype)",
                user.getName(), user.getSurname(), user.getEmail(), user.getAddress(),
                user.getPhoneNumber(), user.getPassword(), user.getUserType().name());
    }

    @Override
    public void update(UserDTO user) {
        jdbcTemplate.update("UPDATE users SET user_name = ?, user_surname = ?, email = ?, address = ?, phone_number = ? " +
                " WHERE email = ?", user.getUserName(), user.getUserSurname(), user.getEmail(), user.getAddress(), user.getPhoneNumber(), user.getEmail());
    }

    @Override
    public void delete(User user) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new Object[]{email}, new UserRowMapper()).stream().findFirst();
    }

    @Override
    public void addRecipeToFavoritesById(Long id, Long recipeId) {
        jdbcTemplate.update("INSERT INTO user_favorite_recipes(user_id, recipe_id)" +
                "VALUES (?, ?)", id, recipeId);
    }

    @Override
    public boolean alreadyFavorited(Long id, Long recipeId) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user_favorite_recipes WHERE user_id = ? AND recipe_id = ?", new Object[]{id, recipeId}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public void removeRecipeFromFavoriteById(Long id, Long recipeId) {
        jdbcTemplate.update("DELETE FROM user_favorite_recipes WHERE user_id = ? AND recipe_id = ?", id, recipeId);
    }

    @Override
    public UserType getUserTypeById(Long userId) {
        return jdbcTemplate.queryForObject("SELECT user_type FROM users WHERE id = ?", new Object[]{userId}, (rs, rowNum) -> {
            String userTypeString = rs.getString("user_type");
            return UserType.valueOf(userTypeString);
        });
    }

    @Override
    public Optional<User> findDeliveryPersonByUserId(Long deliveryPersonId) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ? AND user_type = 'DeliveryPerson'",
                new Object[]{deliveryPersonId}, new UserRowMapper()).stream().findFirst();
    }

    @Override
    public boolean updateUserToDeliveryById(Long userId) {
        int affectedRows = jdbcTemplate.update("UPDATE users SET user_type = ?::UserType WHERE id = ?", UserType.DeliveryPerson.name() ,userId);
        return affectedRows == 1;
    }

}
