package it.project.cookcraft.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long quantity;
    private LocalDateTime createdDate;
}
