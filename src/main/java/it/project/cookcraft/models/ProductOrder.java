package it.project.cookcraft.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder {
    private Long id;
    private Product product;
    private Order order;
    private DeliveryPerson deliveryPerson;
    private LocalDateTime createdDate;
    private int quantity;
}
