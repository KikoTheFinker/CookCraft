package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.UserDAO;
import it.project.cookcraft.models.User;
import it.project.cookcraft.models.UserType;
import org.springframework.beans.factory.annotation.Autowired;
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
                        "VALUES (?, ?, ?, ?, ?, ?, ?)", user.getName(), user.getSurname(), user.getEmail(), user.getAddress(),
                user.getPhoneNumber(), user.getPassword(), user.getUserType().name());
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("UPDATE users SET user_name = ?, user_surname = ?, email = ?, address = ?, phone_number = ?, password = ?, " +
                        "user_type = ? WHERE id = ?", user.getName(), user.getSurname(), user.getEmail(), user.getAddress(), user.getPhoneNumber(),
                user.getPassword(), user.getUserType().name(), user.getId());
    }

    @Override
    public void delete(User user) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new Object[]{email}, new UserRowMapper()).stream().findFirst();
    }
}
