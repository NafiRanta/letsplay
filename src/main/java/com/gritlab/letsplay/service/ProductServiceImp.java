package com.gritlab.letsplay.service;


import com.gritlab.letsplay.config.FieldValidator;
import com.gritlab.letsplay.exception.ProductCollectionException;
import com.gritlab.letsplay.model.Product;
import com.gritlab.letsplay.model.User;
import com.gritlab.letsplay.repository.ProductRepository;
import com.gritlab.letsplay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void createProduct(Product product, UserDetails userDetails) throws ConstraintViolationException, ProductCollectionException {
       Optional<User> userOptional = userRepository.findByUser(userDetails.getUsername());

            if (product.getId() != null){
                product.setId(product.uuidGenerator());
            }
            if (userOptional.isEmpty()) {
                throw new ProductCollectionException(ProductCollectionException.UserNotFoundException());
            } else if (userDetails.getUsername().equals(userOptional.get().getEmail())){
                product.setUserId(userOptional.get().getId());
                FieldValidator.validateProduct(product);
                productRepository.save(product);
            } else {
                throw new ProductCollectionException(ProductCollectionException.AccessDeniedException());
            }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()){
            return products;
        } else{
            return new ArrayList<Product>();
        }
    }

    @Override
    public Product getSingleProduct(String id) throws ProductCollectionException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new ProductCollectionException(ProductCollectionException.NotFoundException(id));
        } else{
            return productOptional.get();
        }
    }

    @Override
    public void updateProduct(String id, Product product) throws ProductCollectionException {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            System.out.println("product userId: " + productOptional.get().getUserId());
            FieldValidator.validateProduct(product);

            if (productOptional.get().getName().equals(product.getName()) &&
            productOptional.get().getDescription().equals(product.getDescription()) &&
            productOptional.get().getPrice().equals(product.getPrice())){
                throw new ProductCollectionException(ProductCollectionException.NoChangesMadeException());
            }

            Product productUpdate = productOptional.get();
            productUpdate.setName(product.getName());
            productUpdate.setDescription(product.getDescription());
            productUpdate.setPrice(product.getPrice());
            productUpdate.setUserId(productUpdate.getUserId());
            productRepository.save(productUpdate);
        } else {
            throw new ProductCollectionException(ProductCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void deleteProductById(String id) throws ProductCollectionException {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()){
            throw new ProductCollectionException(ProductCollectionException.NotFoundException(id));
        } else{
            productRepository.deleteById(id);
        }
    }
}
