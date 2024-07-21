package it.project.cookcraft.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "product_order")
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "product_id")
    private Product product;

    @OneToOne(mappedBy = "order_id")
    private Order order;

    @OneToOne(mappedBy = "delivery_person")
    private DeliveryPerson deliveryPerson;

    @CreatedDate
    @Column(name = "order_date")
    private LocalDateTime createdDate;

    @Column(name = "quantity")
    private int quantity;
}
