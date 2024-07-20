/*
package it.project.cookcraft.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_order")
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "productid")
    private Product product;

    @OneToOne(mappedBy = "orderid")
    private Order order;

    */
/*@OneToOne(mappedBy = "deliveryPerson")
    private DeliveryPerson deliveryPerson;*//*


    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "quantity")
    private int quantity;
}
*/
