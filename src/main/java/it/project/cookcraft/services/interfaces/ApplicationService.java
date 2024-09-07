package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    List<Application> findAllApplications();
    Optional<Application> findApplicationById(Long id);
    void saveApplication(Application application);
    void updateApplication(Application application);
    void deleteApplication(Application application);
    Page<Application> findAllApplications(Pageable pageable);
}
