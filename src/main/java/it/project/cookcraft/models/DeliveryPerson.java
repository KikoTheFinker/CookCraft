package it.project.cookcraft.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPerson {
    private Long id;
    private boolean active;
    private double totalDistance;
    private Long userId;
    private Long productOrderId;
}
