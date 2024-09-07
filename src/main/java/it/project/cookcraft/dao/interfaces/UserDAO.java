package it.project.cookcraft.dao.interfaces;

import it.project.cookcraft.dto.UserDTO;
import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.models.User;
import it.project.cookcraft.models.UserType;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> findAll();
    Optional<User> findById(Long id);
    void save(User user);
    void update(UserDTO user);
    void delete(User user);
    Optional<User> findUserByEmail(String email);
    void addRecipeToFavoritesById(Long id, Long recipeId);
    boolean alreadyFavorited(Long id, Long recipeId);
    void removeRecipeFromFavoriteById(Long id, Long recipeId);
    UserType getUserTypeById(Long userId);
}
