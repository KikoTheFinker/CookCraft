package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.DeliveryPersonDAO;
import it.project.cookcraft.models.DeliveryPerson;
import it.project.cookcraft.services.interfaces.DeliveryPersonService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryPersonServiceImpl implements DeliveryPersonService {

    private final DeliveryPersonDAO deliveryPersonDAO;

    public DeliveryPersonServiceImpl(DeliveryPersonDAO deliveryPersonDAO) {
        this.deliveryPersonDAO = deliveryPersonDAO;
    }

    @Override
    public void saveDeliveryPerson(DeliveryPerson deliveryPerson) {
        deliveryPersonDAO.save(deliveryPerson);
    }

    @Override
    public Optional<DeliveryPerson> findById(Long id) {
        return deliveryPersonDAO.findById(id);
    }

    @Override
    public Optional<DeliveryPerson> findByUserId(Long userId) {
        return deliveryPersonDAO.findByUserId(userId);
    }

    @Override
    public void updateDeliveryPerson(DeliveryPerson deliveryPerson) {
        deliveryPersonDAO.update(deliveryPerson);
    }
}
