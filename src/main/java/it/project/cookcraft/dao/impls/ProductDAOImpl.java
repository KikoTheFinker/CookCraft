package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.ProductDAO;
import it.project.cookcraft.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDAOImpl implements ProductDAO {

    private final JdbcTemplate jdbcTemplate;

    public ProductDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setName(rs.getString("product_name"));
            product.setPrice(rs.getDouble("price"));

            return product;
        }
    }


    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM product", new ProductRowMapper());
    }

    @Override
    public Optional<Product> findById(int id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM product WHERE id = ?", new ProductRowMapper(), id));
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update("INSERT INTO product (name, price) VALUES (?, ?)", product.getName(), product.getPrice());
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update("UPDATE product SET name = ?, price = ? WHERE id = ?", product.getName(), product.getPrice());
    }

    @Override
    public void delete(Product product) {
        jdbcTemplate.update("DELETE FROM product WHERE id = ?", product.getId());
    }
}
