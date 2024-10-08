package it.project.cookcraft.dao.interfaces;

import it.project.cookcraft.models.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ApplicationDAO {
    List<Application> findAll();
    Optional<Application> findById(Long id);
    void save(Application application);
    void update(Application application);
    void delete(Application application);
    Page<Application> findAllApplications(Pageable pageable);
}
