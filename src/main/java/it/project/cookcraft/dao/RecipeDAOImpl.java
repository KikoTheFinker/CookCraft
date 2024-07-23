package it.project.cookcraft.dao;

import it.project.cookcraft.models.Recipe;

import java.util.List;
import java.util.Optional;

public class RecipeDAOImpl implements RecipeDAO {



    @Override
    public List<Recipe> findAll() {
        return List.of();
    }

    @Override
    public Optional<Recipe> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void save(Recipe recipe) {

    }

    @Override
    public void delete(Recipe recipe) {

    }

    @Override
    public void update(Recipe recipe) {

    }
}
