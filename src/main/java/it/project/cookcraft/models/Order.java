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
    private double distance;
    private double price;
    private Long userId;
    private boolean isFinished;
    private List<Product> products;
}
