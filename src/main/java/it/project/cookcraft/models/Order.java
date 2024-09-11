package it.project.cookcraft.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String address;
    private String review;
    private int rating;
    private boolean isFinished;
    private Long userId;
    private Long deliveryPersonId;
    private List<ProductOrder> productOrders;
}
