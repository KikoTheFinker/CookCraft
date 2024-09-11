package it.project.cookcraft.dao.interfaces;

import it.project.cookcraft.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderDAO {
    void save(Order order);
    Optional<Order> findById(Long id);
    Page<Order> findAllOrders(Pageable pageable);

    Page<Order> findAllFinishedOrdersWithReviews(Pageable pageable);
}
