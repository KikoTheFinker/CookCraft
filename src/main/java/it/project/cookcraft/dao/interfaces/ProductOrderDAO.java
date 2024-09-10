package it.project.cookcraft.dao.interfaces;

import it.project.cookcraft.models.ProductOrder;

import java.util.List;

public interface ProductOrderDAO {
    void save(ProductOrder productOrder);
    List<ProductOrder> findByOrderId(Long orderId);
    List<ProductOrder> findProductOrdersByDeliveryPersonId(Long deliveryPersonId);
}
