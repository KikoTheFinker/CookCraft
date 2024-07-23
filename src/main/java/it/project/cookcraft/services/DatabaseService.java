package it.project.cookcraft.services;

import it.project.cookcraft.dao.UserDAOimpl;
import it.project.cookcraft.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> getAllUsers() {
        UserDAOimpl userDAO = new UserDAOimpl(jdbcTemplate);
        return userDAO.getAll();
    }
}
