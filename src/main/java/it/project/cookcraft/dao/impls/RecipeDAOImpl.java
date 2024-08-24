package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.RecipeDAO;
import it.project.cookcraft.dto.ProductDTO;
import it.project.cookcraft.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RecipeDAOImpl implements RecipeDAO {

    private final JdbcTemplate jdbcTemplate;

    public RecipeDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class RecipeRowMapper implements RowMapper<Recipe> {
        @Override
        public Recipe mapRow(ResultSet rs, int rowNum) throws SQLException {
            Recipe recipe = new Recipe();
            recipe.setId(rs.getLong("id"));
            recipe.setName(rs.getString("recipe_name"));
            recipe.setDescription(rs.getString("description"));
            recipe.setCategory(rs.getString("category"));
            recipe.setOrigin(rs.getString("origin"));
            recipe.setMealThumb(rs.getString("meal_thumb"));
            recipe.setVideoUrl(rs.getString("video_url"));

            return recipe;
        }
    }

    @Override
    public List<Recipe> findAll() {
        return jdbcTemplate.query("SELECT * FROM recipe", new RecipeRowMapper());
    }

    @Override
    public Page<Recipe> findAll(Pageable pageable) {
        Integer totalRows = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM recipe", Integer.class);

        int totalRowsCount = (totalRows != null) ? totalRows : 0;

        List<Recipe> recipes = jdbcTemplate.query(
                "SELECT * FROM recipe ORDER BY id ASC LIMIT ? OFFSET ?",
                (PreparedStatement ps) -> {
                    ps.setInt(1, pageable.getPageSize());
                    ps.setInt(2, (int) pageable.getOffset());
                },
                new RecipeRowMapper()
        );

        return new PageImpl<>(recipes, pageable, totalRowsCount);
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        List<Recipe> recipes = jdbcTemplate.query(
                "SELECT * FROM recipe WHERE id = ?",
                ps -> ps.setLong(1, id),
                new RecipeRowMapper()
        );
        return recipes.stream().findFirst();
    }

    @Override
    public void save(Recipe recipe) {
        jdbcTemplate.update(
                "INSERT INTO recipe (recipe_name, description, category, origin, meal_thumb, video_url) VALUES (?, ?, ?, ?, ?, ?)",
                recipe.getName(), recipe.getDescription(), recipe.getCategory(), recipe.getOrigin(),
                recipe.getMealThumb(), recipe.getVideoUrl()
        );
    }

    @Override
    public void update(Recipe recipe) {
        jdbcTemplate.update(
                "UPDATE recipe SET recipe_name = ?, description = ?, category = ?, origin = ?, meal_thumb = ?, video_url = ? WHERE id = ?",
                recipe.getName(), recipe.getDescription(), recipe.getCategory(), recipe.getOrigin(),
                recipe.getMealThumb(), recipe.getVideoUrl(), recipe.getId()
        );
    }

    @Override
    public void delete(Recipe recipe) {
        jdbcTemplate.update("DELETE FROM recipe WHERE id = ?", recipe.getId());
    }

    @Override
    public Page<Recipe> findRecipesByNationality(String nationality, Pageable pageable) {
        Integer totalRows = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM recipe WHERE origin = ?",
                new Object[]{nationality},
                Integer.class
        );

        int totalRowsCount = (totalRows != null) ? totalRows : 0;

        List<Recipe> recipes = jdbcTemplate.query(
                "SELECT * FROM recipe WHERE origin = ? ORDER BY id ASC LIMIT ? OFFSET ?",
                ps -> {
                    ps.setString(1, nationality);
                    ps.setInt(2, pageable.getPageSize());
                    ps.setInt(3, (int) pageable.getOffset());
                },
                new RecipeRowMapper()
        );

        return new PageImpl<>(recipes, pageable, totalRowsCount);
    }


    @Override
    public Page<Recipe> findRecipesByCategory(String category, Pageable pageable) {
        String sql;
        String countSql;

        switch (category.toLowerCase()) {
            case "breakfast":
                sql = "SELECT * FROM recipe WHERE category = 'Breakfast' ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category = 'Breakfast'";
                break;
            case "lunch":
                sql = "SELECT * FROM recipe WHERE category IN ('Chicken', 'Vegetarian', 'Vegan', 'Pasta', 'Seafood', 'Pork', 'Side') ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category IN ('Chicken', 'Vegetarian', 'Vegan', 'Pasta', 'Seafood', 'Pork', 'Side')";
                break;
            case "dinner":
                sql = "SELECT * FROM recipe WHERE category IN ('Beef', 'Lamb', 'Chicken', 'Vegetarian', 'Vegan', 'Pasta', 'Seafood', 'Pork', 'Goat', 'Side') ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category IN ('Beef', 'Lamb', 'Chicken', 'Vegetarian', 'Vegan', 'Pasta', 'Seafood', 'Pork', 'Goat', 'Side')";
                break;
            case "dessert":
                sql = "SELECT * FROM recipe WHERE category = 'Dessert' ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category = 'Dessert'";
                break;
            case "vegetarian":
                sql = "SELECT * FROM recipe WHERE category = 'Vegetarian' ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category = 'Vegetarian'";
                break;
            case "pescatarian":
                sql = "SELECT * FROM recipe WHERE category IN ('Seafood', 'Vegetarian', 'Vegan', 'Side') ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category IN ('Seafood', 'Vegetarian', 'Vegan', 'Side')";
                break;
            default:
                throw new IllegalArgumentException("Invalid meal type: " + category);
        }

        Integer totalRows = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalRowsCount = (totalRows != null) ? totalRows : 0;

        List<Recipe> recipes = jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setInt(1, pageable.getPageSize());
                    ps.setInt(2, (int) pageable.getOffset());
                },
                new RecipeRowMapper()
        );

        return new PageImpl<>(recipes, pageable, totalRowsCount);
    }
    @Override
    public Page<Recipe> findRecipesByNationalityAndCategory(String nationality, String category, Pageable pageable) {
        String sql;
        String countSql;

        switch (category.toLowerCase()) {
            case "breakfast":
                sql = "SELECT * FROM recipe WHERE category = 'Breakfast' AND origin = ? ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category = 'Breakfast' AND origin = ?";
                break;
            case "lunch":
                sql = "SELECT * FROM recipe WHERE category IN ('Chicken', 'Vegetarian', 'Vegan', 'Pasta', 'Seafood', 'Pork', 'Side') AND origin = ? ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category IN ('Chicken', 'Vegetarian', 'Vegan', 'Pasta', 'Seafood', 'Pork', 'Side') AND origin = ?";
                break;
            case "dinner":
                sql = "SELECT * FROM recipe WHERE category IN ('Beef', 'Lamb', 'Chicken', 'Vegetarian', 'Vegan', 'Pasta', 'Seafood', 'Pork', 'Goat', 'Side') AND origin = ? ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category IN ('Beef', 'Lamb', 'Chicken', 'Vegetarian', 'Vegan', 'Pasta', 'Seafood', 'Pork', 'Goat', 'Side') AND origin = ?";
                break;
            case "dessert":
                sql = "SELECT * FROM recipe WHERE category = 'Dessert' AND origin = ? ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category = 'Dessert' AND origin = ?";
                break;
            case "vegetarian":
                sql = "SELECT * FROM recipe WHERE category = 'Vegetarian' AND origin = ? ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category = 'Vegetarian' AND origin = ?";
                break;
            case "pescatarian":
                sql = "SELECT * FROM recipe WHERE category IN ('Seafood', 'Vegetarian', 'Vegan', 'Pasta', 'Side') AND origin = ? ORDER BY id ASC LIMIT ? OFFSET ?";
                countSql = "SELECT COUNT(*) FROM recipe WHERE category IN ('Seafood', 'Vegetarian', 'Vegan', 'Pasta', 'Side') AND origin = ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid meal type: " + category);
        }

        Integer totalRows = jdbcTemplate.queryForObject(countSql, Integer.class, nationality);
        int totalRowsCount = (totalRows != null) ? totalRows : 0;

        List<Recipe> recipes = jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setString(1, nationality);
                    ps.setInt(2, pageable.getPageSize());
                    ps.setInt(3, (int) pageable.getOffset());
                },
                new RecipeRowMapper()
        );

        return new PageImpl<>(recipes, pageable, totalRowsCount);
    }

    @Override
    public List<ProductDTO> findProductsByRecipeId(Long recipeId) {
        String sqlQuery = "SELECT pir.product_id, p.product_name, p.price, pir.measurement " +
                "FROM products_in_recipe pir " +
                "JOIN product p ON pir.product_id = p.id " +
                "WHERE pir.recipe_id = ?";

        List<ProductDTO> products = new ArrayList<>();
        jdbcTemplate.query(sqlQuery, new Object[]{recipeId}, (rs, rowNum) -> {
            ProductDTO product = new ProductDTO();
            product.setId(rs.getLong("product_id"));
            product.setName(rs.getString("product_name"));
            product.setPrice(rs.getDouble("price"));
            product.setMeasurement(rs.getString("measurement"));

            products.add(product);
            return product;
        });

        return products;
    }



}