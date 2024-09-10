package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.DeliveryPerson;

import java.util.Optional;

public interface DeliveryPersonService {
    void saveDeliveryPerson(DeliveryPerson deliveryPerson);
    Optional<DeliveryPerson> findById(Long id);
    Optional<DeliveryPerson> findByUserId(Long userId);
    void updateDeliveryPerson(DeliveryPerson deliveryPerson);
}
