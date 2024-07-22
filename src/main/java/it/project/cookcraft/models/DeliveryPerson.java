package it.project.cookcraft.models;


import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "delivery_person")
@Data
@NoArgsConstructor
//@PrimaryKeyJoinColumn(name = "user_id")
public class DeliveryPerson extends User{
    private int distanceCovered;
    private boolean active;

    @OneToOne(mappedBy = "users")
    private User user;

    @OneToOne(mappedBy = "order")
    private ProductOrder order;
}
