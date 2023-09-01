package com.gritlab.letsplay.exception;

import java.io.Serial;

public class UserCollectionException extends Exception {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public UserCollectionException(String message) {
        super(message);
    }

    public static String NotFoundException(String id){
        return "User with " + id + " not found!";
    }

    public static String UserAlreadyExistException(){
        return "User already exist!";
    }

    public static String NullException(){
        return " cannot be empty!";
    }

    public static String InvalidEmailException(){return "Invalid email format!";}

    public static String InvalidRoleException(){return " has to be in either ROLE_ADMIN or ROLE_USER format!";}
}
