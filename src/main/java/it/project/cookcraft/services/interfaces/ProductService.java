package it.project.cookcraft.services.interfaces;

import it.project.cookcraft.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();
    Optional<Product> findProductById(int id);
    void saveProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Product product);
}
