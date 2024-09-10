package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.ProductOrderDAO;
import it.project.cookcraft.models.ProductOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProductOrderDAOImpl implements ProductOrderDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProductOrderDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private static class ProductOrderRowMapper implements RowMapper<ProductOrder> {
        @Override
        public ProductOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setId(rs.getLong("id"));
            productOrder.setOrderId(rs.getLong("order_id"));
            productOrder.setProductId(rs.getLong("product_id"));
            productOrder.setDeliveryPersonId(rs.getObject("delivery_person") != null ? rs.getLong("delivery_person") : null);
            productOrder.setCreatedDate(rs.getTimestamp("order_date").toLocalDateTime());
            productOrder.setQuantity(rs.getLong("quantity"));
            return productOrder;
        }
    }

    @Override
    public void save(ProductOrder productOrder) {
        String sql = "INSERT INTO product_order (order_id, product_id, delivery_person, order_date, quantity) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                productOrder.getOrderId(),
                productOrder.getProductId(),
                productOrder.getDeliveryPersonId(),
                LocalDateTime.now(),
                productOrder.getQuantity()
        );
    }

    @Override
    public List<ProductOrder> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM product_order WHERE order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, new ProductOrderRowMapper());
    }

    @Override
    public List<ProductOrder> findProductOrdersByDeliveryPersonId(Long deliveryPersonId) {
        String sql;
        Object[] params;

        if (deliveryPersonId == null) {
            sql = "SELECT * FROM product_order WHERE delivery_person IS NULL";
            params = new Object[]{};
        } else {
            sql = "SELECT * FROM product_order WHERE delivery_person = ?";
            params = new Object[]{deliveryPersonId};
        }

        return jdbcTemplate.query(sql, params, new ProductOrderRowMapper());
    }



}
