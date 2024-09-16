package it.project.cookcraft.dto;

import it.project.cookcraft.models.Order;
import it.project.cookcraft.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedOrderDTO {
    private Order order;
    private User user;
    private User deliveryPerson;
}