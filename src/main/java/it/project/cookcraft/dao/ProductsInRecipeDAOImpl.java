package it.project.cookcraft.dao;

import it.project.cookcraft.models.ProductsInRecipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ProductsInRecipeDAOImpl implements ProductsInRecipeDAO {

    JdbcTemplate jdbcTemplate;


    @Autowired
    public ProductsInRecipeDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class ProductsInRecipeRowMapper implements ResultSetExtractor<List<ProductsInRecipe>> {

        @Override
        public List<ProductsInRecipe> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, ProductsInRecipe> productsInRecipeMap = new HashMap<>();


            while (rs.next()){
                Long recipeId = rs.getLong("recipe_id");

                ProductsInRecipe productsInRecipe = productsInRecipeMap.get(recipeId);
                if (productsInRecipe == null) {
                    productsInRecipe = new ProductsInRecipe();
                    productsInRecipe.setId(recipeId);
                    productsInRecipe.setRecipeId(recipeId);
                    productsInRecipe.setProductIds(new ArrayList<>());
                    productsInRecipe.setMeasurement(new ArrayList<>());
                    productsInRecipeMap.put(recipeId, productsInRecipe);
                }
                productsInRecipe.getProductIds().add(rs.getLong("product_id"));
                productsInRecipe.getMeasurement().add(rs.getString("measurement"));
            }
           return new ArrayList<>(productsInRecipeMap.values());
        }
    }

    @Override
    public List<ProductsInRecipe> findAll() {
        return jdbcTemplate.query("SELECT * FROM products_in_recipe", new ProductsInRecipeRowMapper());
    }

    @Override
    public Optional<ProductsInRecipe> findById(int id) {
        List<ProductsInRecipe> result = jdbcTemplate.query("SELECT * FROM products_in_recipe WHERE recipe_id = ?", new ProductsInRecipeRowMapper(), id);
        return result.stream().findFirst();
    }


    @Override
    public void save(ProductsInRecipe productsInRecipe) {
        for (int i = 0; i < productsInRecipe.getProductIds().size(); i++) {
            jdbcTemplate.update("INSERT INTO products_in_recipe (recipe_id, product_id, measurement) VALUES (?, ?, ?)",
                    productsInRecipe.getRecipeId(), productsInRecipe.getProductIds().get(i), productsInRecipe.getMeasurement().get(i));
        }
    }

    @Override
    public void update(ProductsInRecipe productsInRecipe) {
        jdbcTemplate.update("DELETE FROM products_in_recipe WHERE recipe_id = ?", productsInRecipe.getRecipeId());

        String sql = "INSERT INTO products_in_recipe (recipe_id, product_id, measurement) VALUES (?, ?, ?)";
        for (int i = 0; i < productsInRecipe.getProductIds().size(); i++) {
            jdbcTemplate.update(sql, productsInRecipe.getRecipeId(), productsInRecipe.getProductIds().get(i), productsInRecipe.getMeasurement().get(i));
        }
    }

    @Override
    public void delete(ProductsInRecipe productsInRecipe) {
        jdbcTemplate.update("DELETE FROM products_in_recipe WHERE id = ?", productsInRecipe.getRecipeId());
    }
}
