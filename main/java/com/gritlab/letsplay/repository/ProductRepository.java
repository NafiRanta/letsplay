package com.gritlab.letsplay.repository;

import com.gritlab.letsplay.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    @Query("{'id': ?0}")
    Optional<Product> findByProduct(Product product);
}
