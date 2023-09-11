package com.gritlab.letsplay.service;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gritlab.letsplay.config.FieldValidator;
import com.gritlab.letsplay.exception.ProductCollectionException;
import com.gritlab.letsplay.model.Product;
import com.gritlab.letsplay.model.User;
import com.gritlab.letsplay.repository.ProductRepository;
import com.gritlab.letsplay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void createProduct(Product product, UserDetails userDetails) throws ConstraintViolationException, ProductCollectionException {
        FieldValidator.validateProduct(product);

            if (product.getId() != null){
                product.setId(product.uuidGenerator());
            }
            Optional<User> userOptional = userRepository.findByUser(userDetails.getUsername());
            if (userOptional.isEmpty()) {
                throw new ProductCollectionException(ProductCollectionException.UserNotFoundException());
            } else if (userDetails.getUsername().equals(userOptional.get().getEmail())){
                product.setUserId(userOptional.get().getId());
                productRepository.save(product);
            } else {
                throw new ProductCollectionException(ProductCollectionException.AccessDeniedException());
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

        // if product.getName().trim() et al is not null, trim, else throw null exception
        FieldValidator.validateProduct(product);

        Optional<User> userOptional = userRepository.findById(product.getUserId().trim());

        if (productOptional.isPresent()) {
            if (productOptional.get().getName().equals(product.getName()) &&
            productOptional.get().getDescription().equals(product.getDescription()) &&
            productOptional.get().getPrice().equals(product.getPrice()) &&
            productOptional.get().getUserId().equals(product.getUserId())){
                throw new ProductCollectionException(ProductCollectionException.NoChangesMadeException());
            } else if(userOptional.isEmpty()){
                throw new ProductCollectionException(ProductCollectionException.UserNotFoundException());
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

    // create method to check for null trimmed product fields to avoid duplication



}
