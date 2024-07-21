package it.project.cookcraft.configs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.project.cookcraft.inserters.MeasurementsInserter;
import it.project.cookcraft.inserters.ProductsInserter;
import it.project.cookcraft.models.Recipe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initData() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM recipe", Integer.class);
        if (count == null || count == 0) {
            fetchDataFromApi();
        }
    }

    private void fetchDataFromApi() {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 52764; i <= 53083; i++) {
            String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + i;
            String response = restTemplate.getForObject(url, String.class);
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response);
                JsonNode mealsNode = root.path("meals");
                if (mealsNode.isArray() && !mealsNode.isEmpty()) {
                    JsonNode mealNode = mealsNode.get(0);
                    Recipe recipe = mapper.treeToValue(mealNode, Recipe.class);
                    ProductsInserter productsInserter = mapper.treeToValue(mealNode, ProductsInserter.class);
                    MeasurementsInserter measurementsInserter = mapper.treeToValue(mealNode, MeasurementsInserter.class);
                    jdbcTemplate.update(
                            "INSERT INTO recipe (recipe_name, description, category, origin, meal_thumb, video_url) VALUES (?, ?, ?, ?, ?, ?)",
                            recipe.getName(), recipe.getDescription(), recipe.getCategory(), recipe.getOrigin(), recipe.getMealThumb(), recipe.getVideoUrl()
                    );
                    int recipeId = jdbcTemplate.queryForObject("SELECT id FROM recipe WHERE recipe_name = ?", new Object[]{recipe.getName()}, Integer.class);
                    insertIngredientsAndMeasurements(productsInserter, measurementsInserter, recipeId);
                }
                Thread.sleep(2000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void insertIngredientsAndMeasurements(ProductsInserter productsInserter, MeasurementsInserter measurementsInserter, int recipeId) {
        String[] ingredients = {
                productsInserter.getIngredient1(), productsInserter.getIngredient2(), productsInserter.getIngredient3(),
                productsInserter.getIngredient4(), productsInserter.getIngredient5(), productsInserter.getIngredient6(),
                productsInserter.getIngredient7(), productsInserter.getIngredient8(), productsInserter.getIngredient9(),
                productsInserter.getIngredient10(), productsInserter.getIngredient11(), productsInserter.getIngredient12(),
                productsInserter.getIngredient13(), productsInserter.getIngredient14(), productsInserter.getIngredient15(),
                productsInserter.getIngredient16(), productsInserter.getIngredient17(), productsInserter.getIngredient18(),
                productsInserter.getIngredient19(), productsInserter.getIngredient20()
        };
        String[] measurements = {
                measurementsInserter.getMeasure1(), measurementsInserter.getMeasure2(), measurementsInserter.getMeasure3(),
                measurementsInserter.getMeasure4(), measurementsInserter.getMeasure5(), measurementsInserter.getMeasure6(),
                measurementsInserter.getMeasure7(), measurementsInserter.getMeasure8(), measurementsInserter.getMeasure9(),
                measurementsInserter.getMeasure10(), measurementsInserter.getMeasure11(), measurementsInserter.getMeasure12(),
                measurementsInserter.getMeasure13(), measurementsInserter.getMeasure14(), measurementsInserter.getMeasure15(),
                measurementsInserter.getMeasure16(), measurementsInserter.getMeasure17(), measurementsInserter.getMeasure18(),
                measurementsInserter.getMeasure19(), measurementsInserter.getMeasure20()
        };

        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] != null && !ingredients[i].trim().isEmpty()) {
                jdbcTemplate.update("INSERT INTO product (product_name) VALUES (?) ON CONFLICT (product_name) DO NOTHING", ingredients[i]);
                int productId = jdbcTemplate.queryForObject("SELECT id FROM product WHERE product_name = ?", new Object[]{ingredients[i]}, Integer.class);
                jdbcTemplate.update("INSERT INTO products_in_recipe (product_id, recipe_id, measurement) VALUES (?, ?, ?) ON CONFLICT (product_id, recipe_id) DO NOTHING", productId, recipeId, measurements[i]);
            }
        }
    }
}
