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
import java.util.stream.Collectors;

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
    public List<ProductDTO> findProductsByRecipeId(Long recipeId) {
        List<ProductDTO> products = new ArrayList<>();
        jdbcTemplate.query("SELECT pir.product_id, p.product_name, p.price, pir.measurement " +
                "FROM products_in_recipe pir " +
                "JOIN product p ON pir.product_id = p.id " +
                "WHERE pir.recipe_id = ?", new Object[]{recipeId}, (rs, rowNum) -> {
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
    @Override
    public Page<Recipe> findRecipesByFilters(String nationality, String category, List<Long> productIds, String searchTerm, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT DISTINCT r.* FROM recipe r");
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (productIds != null && !productIds.isEmpty()) {
            sqlBuilder.append(" JOIN products_in_recipe pir ON r.id = pir.recipe_id");
            conditions.add("pir.product_id IN (" + productIds.stream().map(id -> "?").collect(Collectors.joining(", ")) + ")");
            params.addAll(productIds);
        }


        if (nationality != null && !nationality.isEmpty()) {
            conditions.add("r.origin = ?");
            params.add(nationality);
        }


        if (category != null && !category.isEmpty()) {
            switch (category.toLowerCase()) {
                case "breakfast":
                    conditions.add("r.category = 'Breakfast'");
                    break;
                case "lunch":
                    conditions.add("r.category IN ('Chicken', 'Vegetarian', 'Vegan', 'Pasta', 'Seafood', 'Pork', 'Side')");
                    break;
                case "dinner":
                    conditions.add("r.category IN ('Beef', 'Lamb', 'Chicken', 'Vegetarian', 'Vegan', 'Pasta', 'Seafood', 'Pork', 'Goat', 'Side')");
                    break;
                case "dessert":
                    conditions.add("r.category = 'Dessert'");
                    break;
                case "vegetarian":
                    conditions.add("r.category = 'Vegetarian'");
                    break;
                case "pescatarian":
                    conditions.add("r.category IN ('Seafood', 'Vegetarian', 'Vegan', 'Side')");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid meal type: " + category);
            }
        }


        if (searchTerm != null && !searchTerm.isEmpty()) {
            conditions.add("LOWER(r.recipe_name) LIKE LOWER(?)");
            params.add("%" + searchTerm + "%");
        }

        if (!conditions.isEmpty()) {
            sqlBuilder.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        String countSql = "SELECT COUNT(*) FROM (" + sqlBuilder + ") AS countQueryWithoutPagination";
        Integer totalRows = jdbcTemplate.queryForObject(countSql, params.toArray(), Integer.class);

        sqlBuilder.append(" ORDER BY r.id ASC LIMIT ? OFFSET ?");
        params.add(pageable.getPageSize());
        params.add((int) pageable.getOffset());

        List<Recipe> recipes = jdbcTemplate.query(sqlBuilder.toString(), params.toArray(), new RecipeRowMapper());

        return new PageImpl<>(recipes, pageable, totalRows != null ? totalRows : 0);
    }

}