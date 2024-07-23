package it.project.cookcraft.dao;

import it.project.cookcraft.models.Recipe;
import java.util.List;
import java.util.Optional;

public interface RecipeDAO {
    List<Recipe> findAll();
    Optional<Recipe> findById(int id);
    void save(Recipe recipe);
    void delete(Recipe recipe);
    void update(Recipe recipe);
}
