package com.gritlab.letsplay.exception;

import java.io.Serial;

public class ProductCollectionException extends Exception {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public ProductCollectionException(String message) {
        super(message);
    }

    public static String NotFoundException(String id){
        return "Product with " + id + " not found!";
    }

    public static String ProductAlreadyExistException(){
        return "Product already exist!";
    }
}
