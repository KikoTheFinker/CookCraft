package it.project.cookcraft.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPerson extends User{
    private int distanceCovered;
    private boolean active;
    private ProductOrder order;
}
