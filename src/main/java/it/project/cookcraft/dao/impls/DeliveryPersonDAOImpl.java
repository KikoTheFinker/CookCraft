package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.DeliveryPersonDAO;
import it.project.cookcraft.models.DeliveryPerson;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@Primary
public class DeliveryPersonDAOImpl implements DeliveryPersonDAO {

    private final JdbcTemplate jdbcTemplate;

    public DeliveryPersonDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private DeliveryPerson mapRowToDeliveryPerson(ResultSet rs) throws SQLException {
        return new DeliveryPerson(
                rs.getLong("id"),
                rs.getBoolean("active"),
                rs.getDouble("total_distance"),
                rs.getLong("user_id"),
                rs.getLong("product_order_id")
        );
    }

    @Override
    public Optional<DeliveryPerson> findDeliveryPersonByUserId(Long userId) {
        String sql = "SELECT * FROM delivery_person WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, rs -> {
            if (rs.next()) {
                return Optional.of(mapRowToDeliveryPerson(rs));
            }
            return Optional.empty();
        });
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
        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
            if (rs.next()) {
                return Optional.of(mapRowToDeliveryPerson(rs));
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<DeliveryPerson> findByUserId(Long userId) {
        return findDeliveryPersonByUserId(userId);
    }

    @Override
    public void update(DeliveryPerson deliveryPerson) {
        String sql = "UPDATE delivery_person SET active = ?, total_distance = ?, user_id = ?, product_order_id = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                deliveryPerson.isActive(),
                deliveryPerson.getTotalDistance(),
                deliveryPerson.getUserId(),
                deliveryPerson.getProductOrderId(),
                deliveryPerson.getId());
    }
}
