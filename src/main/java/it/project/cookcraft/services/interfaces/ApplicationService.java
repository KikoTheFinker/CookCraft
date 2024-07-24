package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.Application;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    List<Application> findAllApplications();
    Optional<Application> findApplicationById(Long id);
    void saveApplication(Application application);
    void updateApplication(Application application);
    void deleteApplication(Application application);
}
