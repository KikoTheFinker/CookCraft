package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.Order;
import java.util.Optional;

public interface OrderService {
    Order saveOrder(Order order);
    Optional<Order> findOrderById(Long id);
}
