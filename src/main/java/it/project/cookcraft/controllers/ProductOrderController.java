package it.project.cookcraft.controllers;

import it.project.cookcraft.models.ProductOrder;
import it.project.cookcraft.services.interfaces.ProductOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productOrders")
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    public ProductOrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    @PostMapping
    public ResponseEntity<String> saveProductOrders(@RequestBody List<ProductOrder> productOrders) {
        productOrders.forEach(productOrderService::saveProductOrder);
        return ResponseEntity.ok("Product orders saved successfully");
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ProductOrder>> findProductOrdersByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(productOrderService.findProductOrdersByOrderId(orderId));
    }
}
