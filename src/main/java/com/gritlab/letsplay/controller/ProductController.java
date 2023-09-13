package com.gritlab.letsplay.controller;

import com.gritlab.letsplay.exception.ProductCollectionException;
import com.gritlab.letsplay.model.Product;
import com.gritlab.letsplay.model.User;
import com.gritlab.letsplay.repository.ProductRepository;
import com.gritlab.letsplay.repository.UserRepository;
import com.gritlab.letsplay.service.ProductService;
import com.gritlab.letsplay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/products/all")
    public ResponseEntity<?> getAllProducts(){
        List<Product> productList = productService.getAllProducts();
        return new ResponseEntity<>(productList, productList.size() > 0 ? HttpStatus.OK: HttpStatus.NOT_FOUND);
    }
    @PostMapping("/products/create")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<?> createProduct(
            @RequestBody Product product,
            @AuthenticationPrincipal UserDetails userDetails) throws ProductCollectionException
    {
        try{
            productService.createProduct(product, userDetails);
            return new ResponseEntity<Product>(product, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/products/single/{id}")
    public ResponseEntity<?> getSingleProduct(@PathVariable("id") String id){
        try{
            return new ResponseEntity<>(productService.getSingleProduct(id), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/products/update/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> updateProductById(@PathVariable("id") String id, @RequestBody Product product) throws ProductCollectionException {
        try{
            productService.updateProduct(id, product);
            return new ResponseEntity<>("Update Product with id " + id, HttpStatus.OK);
        } catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/products/delete/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> deleteProductById(@PathVariable("id") String id){
        try{
            productService.deleteProductById(id);
            return new ResponseEntity<>("Successfully deleted product with id " + id, HttpStatus.OK);
        } catch (ProductCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
