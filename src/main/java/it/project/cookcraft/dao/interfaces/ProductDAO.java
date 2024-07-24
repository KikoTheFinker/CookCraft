package it.project.cookcraft.dao.interfaces;

import it.project.cookcraft.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    List<Product> findAll();
    Optional<Product> findById(int id);
    void save(Product product);
    void update(Product product);
    void delete(Product product);
}
