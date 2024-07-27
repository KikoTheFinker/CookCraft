package it.project.cookcraft.dao.interfaces;

import it.project.cookcraft.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> findAll();
    Optional<User> findById(Long id);
    void save(User user);
    void update(User user);
    void delete(User user);
    Optional<User> findUserByEmail(String email);
}
