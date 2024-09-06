package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.dto.UserDTO;
import it.project.cookcraft.models.Recipe;
import it.project.cookcraft.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    Optional<User> findUserById(Long id);
    void saveUser(User user);
    void updateUser(UserDTO user);
    void deleteUser(User user);
    Optional<User> findUserByEmail(String email);
    void addRecipeToFavoritesById(Long id, Long recipeId);
    boolean alreadyFavorited(Long id, Long recipeId);
    void removeRecipeFromFavoriteById(Long id, Long recipeId);
}
