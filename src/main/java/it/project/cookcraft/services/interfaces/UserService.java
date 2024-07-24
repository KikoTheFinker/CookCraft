package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    Optional<User> findUserById(Long id);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
}
