package it.project.cookcraft.dao;

import it.project.cookcraft.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> getAll();
    Optional<User> findById(Long id);
    void save(User user);
    void update(User user);
    void delete(User user);
}
