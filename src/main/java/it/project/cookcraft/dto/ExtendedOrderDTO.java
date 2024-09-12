package it.project.cookcraft.dto;

import it.project.cookcraft.models.Order;
import it.project.cookcraft.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This DTO is for the admin page, in order for the admin to see reviewed
// orders as well as the details about the user and the delivery person
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedOrderDTO {
    private Order order;
    private User user;
    private User deliveryPerson;
}