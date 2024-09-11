package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.OrderDAO;
import it.project.cookcraft.models.Order;
import it.project.cookcraft.services.interfaces.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO;

    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public Order saveOrder(Order order) {
        orderDAO.save(order);
        return order;
    }

    @Override
    public Optional<Order> findOrderById(Long id) {
        return orderDAO.findById(id);
    }

    @Override
    public Page<Order> findAllOrders(Pageable pageable) {
        return orderDAO.findAllOrders(pageable);
    }

    @Override
    public Page<Order> findAllFinishedOrdersWithReviews(Pageable pageable) {
        return orderDAO.findAllFinishedOrdersWithReviews(pageable);
    }
}
