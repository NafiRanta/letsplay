package com.gritlab.letsplay.service;

import com.gritlab.letsplay.exception.ProductCollectionException;
import com.gritlab.letsplay.model.Product;
import com.gritlab.letsplay.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Override
    public void createProduct(Product product) throws ConstraintViolationException, ProductCollectionException, ProductCollectionException {
        Optional<Product> productOptional = productRepository.findByProduct(product);
        System.out.println("productOptional: " + productOptional);
        if(productOptional.isPresent()){
            throw new ProductCollectionException(ProductCollectionException.ProductAlreadyExistException());
        } else{
            productRepository.save(product);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.size() > 0){
            return products;
        } else{
            return new ArrayList<Product>();
        }
    }

    @Override
    public Product getSingleProduct(String id) throws ProductCollectionException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(!productOptional.isPresent()){
            throw new ProductCollectionException(ProductCollectionException.NotFoundException(id));
        } else{
            return productOptional.get();
        }
    }

    @Override
    public void updateProduct(String id, Product product) throws ProductCollectionException {
        Optional<Product> productOptional = productRepository.findById(id);
        System.out.println("productOptional update product" + productOptional);
        Optional<Product> productOptionalSameName = productRepository.findByProduct(product);
        System.out.println("productOptionalSameName" + productOptionalSameName);

        if (productOptional.isPresent()) {
            if (productOptionalSameName.isPresent() && !productOptionalSameName.get().getId().equals(id)){
                throw new ProductCollectionException(ProductCollectionException.ProductAlreadyExistException());
            }
            Product productUpdate = productOptional.get();
            productUpdate.setName(product.getName());
            productUpdate.setDescription(product.getDescription());
            productUpdate.setPrice(product.getPrice());
            productUpdate.setUserId(product.getUserId());
            productRepository.save(productUpdate);
        } else {
            throw new ProductCollectionException(ProductCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void deleteProductById(String id) throws ProductCollectionException {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()){
            throw new ProductCollectionException(ProductCollectionException.NotFoundException(id));
        } else{
            productRepository.deleteById(id);
        }
    }
}
