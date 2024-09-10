package it.project.cookcraft.dao.interfaces;

import it.project.cookcraft.models.DeliveryPerson;

import java.util.Optional;

public interface DeliveryPersonDAO {
    Optional<DeliveryPerson> findDeliveryPersonByUserId(Long userId);
    void save(DeliveryPerson deliveryPerson);
    Optional<DeliveryPerson> findById(Long id);
    Optional<DeliveryPerson> findByUserId(Long userId);
    void update(DeliveryPerson deliveryPerson);
}
