package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.ApplicationDAO;
import it.project.cookcraft.models.Application;
import it.project.cookcraft.services.interfaces.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {


    private final ApplicationDAO applicationDAO;

    public ApplicationServiceImpl(ApplicationDAO applicationDAO) {
        this.applicationDAO = applicationDAO;
    }

    @Override
    public List<Application> findAllApplications() {
        return applicationDAO.findAll();
    }

    @Override
    public Optional<Application> findApplicationById(Long id) {
        return applicationDAO.findById(id);
    }

    @Override
    public void saveApplication(Application application) {
        applicationDAO.save(application);
    }

    @Override
    public void updateApplication(Application application) {
        applicationDAO.update(application);
    }

    @Override
    public void deleteApplication(Application application) {
        applicationDAO.delete(application);
    }
}
