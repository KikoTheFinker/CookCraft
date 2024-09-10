package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.ProductOrderDAO;
import it.project.cookcraft.models.ProductOrder;
import it.project.cookcraft.services.interfaces.ProductOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final ProductOrderDAO productOrderDAO;

    public ProductOrderServiceImpl(ProductOrderDAO productOrderDAO) {
        this.productOrderDAO = productOrderDAO;
    }

    @Override
    public void saveProductOrder(ProductOrder productOrder) {
        productOrderDAO.save(productOrder);
    }

    @Override
    public List<ProductOrder> findProductOrdersByOrderId(Long orderId) {
        return productOrderDAO.findByOrderId(orderId);
    }

    @Override
    public List<ProductOrder> findProductOrdersByDeliveryPersonId(Long DeliveryPersonId) {
        return productOrderDAO.findProductOrdersByDeliveryPersonId(DeliveryPersonId);
    }
}