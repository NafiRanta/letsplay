package com.gritlab.letsplay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    public static String PasswordLengthException(){
        return " cannot be less than 4 characters!";
    }

    public static String InvalidEmailException(){return "Invalid email format!";}

    public static String InvalidPassword(){return "Password must not contain space!";}

    public static String InvalidRoleException(){return " has to be in either ROLE_ADMIN or ROLE_USER format!";}

    public static String BadCredentialsException(){return "Bad credentials!";}

    public static String UsernameNotFound(){return "User not found!";}
}
