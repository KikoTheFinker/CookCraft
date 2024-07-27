package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.UserDAO;
import it.project.cookcraft.models.User;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> findAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public void saveUser(User user) {
        userDAO.save(user);
    }

    @Override
    public void updateUser(User user) {
        userDAO.update(user);
    }

    @Override
    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userDAO.findUserByEmail(email);
    }
}
