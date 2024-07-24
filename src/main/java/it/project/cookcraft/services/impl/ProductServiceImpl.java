package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.ProductDAO;
import it.project.cookcraft.models.Product;
import it.project.cookcraft.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public Optional<Product> findProductById(int id) {
        return productDAO.findById(id);
    }

    @Override
    public void saveProduct(Product product) {
        productDAO.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        productDAO.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        productDAO.delete(product);
    }
}
