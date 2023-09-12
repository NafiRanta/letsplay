package com.gritlab.letsplay.service;

import com.gritlab.letsplay.exception.ProductCollectionException;
import com.gritlab.letsplay.model.Product;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface ProductService {
    public void createProduct(Product product, UserDetails userDetails)throws ConstraintViolationException, ProductCollectionException;

    public List<Product> getAllProducts();

    public Product getSingleProduct(String id) throws ProductCollectionException;

    public void updateProduct(String id, Product product) throws ProductCollectionException;

    public void deleteProductById(String id) throws ProductCollectionException;
}
