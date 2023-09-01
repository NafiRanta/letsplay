package com.gritlab.letsplay.controller;

import com.gritlab.letsplay.exception.ProductCollectionException;
import com.gritlab.letsplay.model.Product;
import com.gritlab.letsplay.repository.ProductRepository;
import com.gritlab.letsplay.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/all")
    public ResponseEntity<?> getAllProducts(){
        List<Product> productList = productService.getAllProducts();
        return new ResponseEntity<>(productList, productList.size() > 0 ? HttpStatus.OK: HttpStatus.NOT_FOUND);
    }
    @PostMapping("/products/create")
    public ResponseEntity<?> createProduct(@RequestBody Product product){
        try {
            productService.createProduct(product);
            return new ResponseEntity<Product>(product, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            System.out.println("ConstraintViolationException " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ProductCollectionException e){
            System.out.println("ProductCollectionException " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
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
    public ResponseEntity<?> updateProductById(@PathVariable("id") String id, @RequestBody Product product){
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
    public ResponseEntity<?> deleteProductById(@PathVariable("id") String id){
        try{
            productService.deleteProductById(id);
            return new ResponseEntity<>("Successfully deleted product with id " + id, HttpStatus.OK);
        } catch (ProductCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }




}
