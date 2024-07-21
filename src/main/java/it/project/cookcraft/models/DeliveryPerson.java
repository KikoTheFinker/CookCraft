package it.project.cookcraft.models;


import jakarta.persistence.OneToOne;
import lombok.Data;


@Data
//@PrimaryKeyJoinColumn(name = "user_id")
public class DeliveryPerson extends User{
    int distanceCovered;
    boolean active;

    @OneToOne(mappedBy = "order")
    ProductOrder order;
}
