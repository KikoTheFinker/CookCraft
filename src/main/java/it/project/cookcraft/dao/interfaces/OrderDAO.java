package it.project.cookcraft.dao.interfaces;

import it.project.cookcraft.models.Order;

import java.util.Optional;

public interface OrderDAO {
    void save(Order order);
    Optional<Order> findById(Long id);
}
