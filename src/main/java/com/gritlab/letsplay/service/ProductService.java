package com.gritlab.letsplay.service;

import com.gritlab.letsplay.exception.ProductCollectionException;
import com.gritlab.letsplay.model.Product;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface ProductService {
    public void createProduct(Product product)throws ConstraintViolationException, ProductCollectionException;

    public List<Product> getAllProducts();

    public Product getSingleProduct(String id) throws ProductCollectionException;

    public void updateProduct(String id, Product product) throws ProductCollectionException;

    public void deleteProductById(String id) throws ProductCollectionException;
}
