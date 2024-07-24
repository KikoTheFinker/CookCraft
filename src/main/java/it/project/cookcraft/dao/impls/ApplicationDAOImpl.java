package it.project.cookcraft.dao.impls;

import it.project.cookcraft.dao.interfaces.ApplicationDAO;
import it.project.cookcraft.models.Application;
import it.project.cookcraft.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ApplicationDAOImpl implements ApplicationDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ApplicationDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class ApplicationMapper implements RowMapper<Application> {
        @Override
        public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
            Application application = new Application();
            application.setId(rs.getLong("id"));
            application.setCv(rs.getBytes("cv"));
            application.setMotivationalLetter(rs.getString("motivational_letter"));

            User user = new User();
            user.setId(rs.getLong("user_id"));
            application.setUserId(user.getId());

            return application;
        }
    }

    @Override
    public List<Application> findAll() {
        return jdbcTemplate.query("SELECT * FROM application", new ApplicationMapper());
    }

    @Override
    public Optional<Application> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM application WHERE id = ?", new Object[]{id},
                new ApplicationMapper()).stream().findFirst();
    }

    @Override
    public void save(Application application) {
        jdbcTemplate.update("INSERT INTO application (cv, motivational_letter, user_id) VALUES (?, ?, ?)",
                application.getCv(), application.getMotivationalLetter(), application.getUserId());
    }

    @Override
    public void update(Application application) {
        jdbcTemplate.update("UPDATE application SET cv = ?, motivational_letter = ?, user_id = ? WHERE id = ?",
                application.getCv(), application.getMotivationalLetter(), application.getUserId(), application.getId());
    }

    @Override
    public void delete(Application application) {
        jdbcTemplate.update("DELETE FROM application WHERE id = ?", application.getId());
    }
}
