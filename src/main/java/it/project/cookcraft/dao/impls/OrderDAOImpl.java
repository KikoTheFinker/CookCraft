package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.OrderDAO;
import it.project.cookcraft.models.Order;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDAOImpl implements OrderDAO {

    private final JdbcTemplate jdbcTemplate;

    public OrderDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class OrderRowMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setAddress(rs.getString("address"));
            order.setUserId(rs.getLong("userid"));
            order.setFinished(rs.getBoolean("isfinished"));
            order.setReview(rs.getString("review"));
            order.setRating(rs.getInt("rating"));
            order.setDeliveryPersonId(rs.getLong("delivery_person_id"));
            return order;
        }
    }

    public Long save(Order order) {
        String sql = "INSERT INTO orders (address, userid, isfinished) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, order.getAddress());
            ps.setLong(2, order.getUserId());
            ps.setBoolean(3, order.isFinished());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id));
    }

    @Override
    public List<Order> findOrdersByUserIdAndIsFinished(Long userId, boolean isFinished) {
        String sql = "SELECT * FROM orders WHERE userid = ? AND isfinished = ?";
        return jdbcTemplate.query(sql, new OrderRowMapper(), userId, isFinished);
    }

    @Override
    public List<Order> findAllActiveOrders() {
        return jdbcTemplate.query("SELECT * FROM orders WHERE isfinished = false", new OrderRowMapper());
    }

    @Override
    public List<Order> findFinishedOrdersByDeliveryPersonId(Long deliveryPersonId) {
        return jdbcTemplate.query("SELECT * FROM orders WHERE delivery_person_id = ? AND isfinished = true", new OrderRowMapper(), deliveryPersonId);
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE orders SET address = ?, userid = ?, isfinished = ?, review = ?, rating = ?, delivery_person_id = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(sql,
                order.getAddress(),
                order.getUserId(),
                order.isFinished(),
                order.getReview(),
                order.getRating(),
                order.getDeliveryPersonId(),
                order.getId()
        );

        if (rowsAffected == 0) {
            throw new EntityNotFoundException("Order not found with id: " + order.getId());
        }
    }

    @Override
    public List<Order> findAllReviewedOrders() {
        String sql = "SELECT * FROM orders WHERE review IS NOT NULL AND rating IS NOT NULL ORDER BY ID DESC";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    @Override
    public List<Order> findAllOrders() {
        String sql = "SELECT * FROM orders ORDER BY ID DESC";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    @Override
    public Page<Order> findAllReviewedOrders(int totalRows, Pageable pageable) {
        String sql = "SELECT * FROM orders WHERE review IS NOT NULL AND rating IS NOT NULL ORDER BY ID DESC LIMIT ? OFFSET ?";
        List<Order> orders = jdbcTemplate.query(sql,
                (PreparedStatement ps) -> {
                    ps.setInt(1, pageable.getPageSize());
                    ps.setInt(2, (int) pageable.getOffset());
        }, new OrderRowMapper());
        return new PageImpl<>(orders, pageable, totalRows);
    }

    @Override
    public Page<Order> findAllOrders(int totalRows, Pageable pageable) {
        String sql = "SELECT * FROM orders ORDER BY ID DESC LIMIT ? OFFSET ?";
        List<Order> orders = jdbcTemplate.query(sql,
                (PreparedStatement ps) -> {
                    ps.setInt(1, pageable.getPageSize());
                    ps.setInt(2, (int) pageable.getOffset());
                }, new OrderRowMapper());
        return new PageImpl<>(orders, pageable, totalRows);
    }

    @Override
    public List<Order> findOrdersByDeliveryPersonIdAndIsNotFinished(Long id) {
        return jdbcTemplate.query("SELECT * FROM orders WHERE delivery_person_id = ? AND isfinished = false", new OrderRowMapper(), id);
    }

}
