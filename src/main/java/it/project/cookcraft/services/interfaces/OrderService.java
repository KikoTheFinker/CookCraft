package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Long save(Order order);
    Optional<Order> findOrderById(Long id);
    List<Order> findOrdersByUserIdAndIsFinished(Long id, boolean isFinished);
    List<Order> findAllActiveOrders();
    List<Order> findFinishedOrdersByDeliveryPersonId(Long deliveryPersonId);
    void update(Order order);
    List<Order> findAllReviewedOrders();
    List<Order> findAllOrders();
    Page<Order> findAllReviewedOrders(int totalRows, Pageable pageable);
    Page<Order> findAllOrders(int totalRows, Pageable pageable);
    List<Order> findOrdersByDeliveryPersonIdAndIsNotFinished(Long id);
    boolean deleteById(Long id);
}
