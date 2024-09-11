package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Long save(Order order);
    Optional<Order> findOrderById(Long id);
    Page<Order> findAllOrders(Pageable pageable);
    Page<Order> findAllFinishedOrdersWithReviews(Pageable pageable);
    List<Order> findOrdersByUserIdAndIsFinished(Long id, boolean isFinished);
}
