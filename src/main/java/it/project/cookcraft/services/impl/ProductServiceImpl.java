package it.project.cookcraft.services.impl;

import it.project.cookcraft.dao.interfaces.ProductDAO;
import it.project.cookcraft.models.Product;
import it.project.cookcraft.services.interfaces.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {


    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    @Override
    public Optional<Product> findProductById(Long id) {
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
