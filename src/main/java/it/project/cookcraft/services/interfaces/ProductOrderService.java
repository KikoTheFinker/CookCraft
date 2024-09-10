package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.ProductOrder;
import java.util.List;

public interface ProductOrderService {
    void saveProductOrder(ProductOrder productOrder);
    List<ProductOrder> findProductOrdersByOrderId(Long orderId);
    List<ProductOrder> findProductOrdersByDeliveryPersonId(Long DeliveryPersonId);
}
