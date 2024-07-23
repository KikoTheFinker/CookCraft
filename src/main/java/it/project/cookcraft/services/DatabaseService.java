package it.project.cookcraft.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getDataById(Long id) {
        return jdbcTemplate.queryForList("SELECT * FROM recipe WHERE id = ?", id);
    }

}
