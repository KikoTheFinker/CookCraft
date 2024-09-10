package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.OrderDAO;
import it.project.cookcraft.models.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class OrderDAOImpl implements OrderDAO {

    private final JdbcTemplate jdbcTemplate;

    public OrderDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Order order) {
        String sql = "INSERT INTO orders (address, userid, isfinished) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, order.getAddress());
            ps.setLong(2, order.getUserId());
            ps.setBoolean(3, order.isFinished());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            order.setId(keyHolder.getKey().longValue());
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setAddress(rs.getString("address"));
                order.setUserId(rs.getLong("userid"));
                order.setFinished(rs.getBoolean("isfinished"));
                return Optional.of(order);
            }
            return Optional.empty();
        });
    }
}
