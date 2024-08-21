package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.RecipeDAO;
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
        Integer totalRows = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM recipe WHERE category = ?",
                new Object[]{category},
                Integer.class
        );
        int totalRowsCount = (totalRows != null) ? totalRows : 0;

        List<Recipe> recipes = jdbcTemplate.query(
                "SELECT * FROM recipe WHERE category = ? ORDER BY id ASC LIMIT ? OFFSET ?",
                ps -> {
                    ps.setString(1, category);
                    ps.setInt(2, pageable.getPageSize());
                    ps.setInt(3, (int) pageable.getOffset());
                },
                new RecipeRowMapper()
        );

        return new PageImpl<>(recipes, pageable, totalRowsCount);
    }


}
