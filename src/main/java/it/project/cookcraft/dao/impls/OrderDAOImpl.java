package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.OrderDAO;
import it.project.cookcraft.models.Application;
import it.project.cookcraft.models.Order;
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
import java.sql.Statement;
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

    @Override
    public void save(Order order) {
        String sql = "INSERT INTO orders (address, userid, isfinished) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{order.getAddress(), order.getUserId(), order.isFinished()}, new OrderRowMapper());
        /*KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, order.getAddress());
            ps.setLong(2, order.getUserId());
            ps.setBoolean(3, order.isFinished());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            order.setId(keyHolder.getKey().longValue());
        }*/
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id));
        /*return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setAddress(rs.getString("address"));
                order.setUserId(rs.getLong("userid"));
                order.setFinished(rs.getBoolean("isfinished"));
                return Optional.of(order);
            }
            return Optional.empty();
        });*/
    }

    @Override
    public Page<Order> findAllOrders(Pageable pageable) {
        String sql = "SELECT \n" +
                "    u.user_name AS userName,\n" +
                "    u.user_surname AS userSurname,\n" +
                "    u.email,\n" +
                "    u.phone_number AS phoneNumber,\n" +
                "    o.address AS orderAddress,\n" +
                "    dp.user_name AS deliveryPersonName,\n" +
                "    dp.user_surname AS deliveryPersonSurname,\n" +
                "    dp.email AS deliveryPersonEmail,\n" +
                "    dp.phone_number AS deliveryPersonPhoneNumber\n" +
                "FROM \n" +
                "    orders o\n" +
                "JOIN \n" +
                "    users u ON o.userid = u.id\n" +
                "LEFT JOIN \n" +
                "    users dp ON o.delivery_person_id = dp.id;\n";
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM orders", Integer.class);
        List<Order> orders = jdbcTemplate.query(sql, new Object[]{pageable.getPageSize(), pageable.getOffset()}, new OrderRowMapper());
        return new PageImpl<>(orders, pageable, count != null ? count : 0);
    }

    @Override
    public Page<Order> findAllFinishedOrdersWithReviews(Pageable pageable) {
        String sql = "SELECT \n" +
                "    u.user_name AS userName,\n" +
                "    u.user_surname AS userSurname,\n" +
                "    u.email,\n" +
                "    u.phone_number AS phoneNumber,\n" +
                "    o.address AS orderAddress,\n" +
                "    dp.user_name AS deliveryPersonName,\n" +
                "    dp.user_surname AS deliveryPersonSurname,\n" +
                "    dp.email AS deliveryPersonEmail,\n" +
                "    dp.phone_number AS deliveryPersonPhoneNumber\n" +
                "FROM \n" +
                "    orders o\n" +
                "JOIN \n" +
                "    users u ON o.userid = u.id\n" +
                "LEFT JOIN \n" +
                "    users dp ON o.delivery_person_id = dp.id\n" +
                "WHERE \n" +
                "    o.review IS NOT NULL\n" +
                "    AND o.rating IS NOT NULL;\n";
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM orders", Integer.class);
        List<Order> orders = jdbcTemplate.query(sql, new Object[]{pageable.getPageSize(), pageable.getOffset()}, new OrderRowMapper());
        return new PageImpl<>(orders, pageable, count != null ? count : 0);
    }
}
