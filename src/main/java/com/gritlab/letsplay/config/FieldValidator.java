package com.gritlab.letsplay.config;

import com.gritlab.letsplay.exception.ProductCollectionException;
import com.gritlab.letsplay.exception.UserCollectionException;
import com.gritlab.letsplay.model.Product;
import com.gritlab.letsplay.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldValidator {
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isValidEmail(String email) {
        // Compile the regex pattern
        Pattern pattern = Pattern.compile(EMAIL_REGEX);

        // Match the input email against the pattern
        Matcher matcher = pattern.matcher(email);

        // Return true if it matches the pattern (valid format), false otherwise
        return matcher.matches();
    }

    public static void validateUser(User user) throws UserCollectionException {
        if (user.getName()!= null) {
            user.setName(user.getName().trim());
        } else {
            throw new UserCollectionException("User name" + UserCollectionException.NullException());
        }
        // if product.getDescription() is not null, trim, else throw exception
        if (user.getEmail()!= null) {
            boolean isValid = isValidEmail(user.getEmail());
            System.out.println(user.getEmail() + " is valid email: " + isValid);
            if (!isValid) {
                throw new UserCollectionException(UserCollectionException.InvalidEmailException());
            }
            user.setEmail(user.getEmail().trim());
        } else {
            throw new UserCollectionException("User email" + UserCollectionException.NullException());
        }
        if (user.getPassword()!= null) {
            user.setPassword(user.getPassword().trim()); // Trim the ID field as well
        } else {
            throw new UserCollectionException("User password" + UserCollectionException.NullException());
        }

        if (user.getRole()!= null){
            // check if role enum is either user.getRole() is "ROLE_ADMIN" or "ROLE_USER"
            if (!(user.getRole().equals("ROLE_ADMIN")  || user.getRole().equals("ROLE_USER"))) {
                throw new UserCollectionException("User role" + UserCollectionException.InvalidRoleException());
            }
            user.setRole(user.getRole().trim());
        }else {
            throw new UserCollectionException("User role" + UserCollectionException.NullException());
        }

    }

    public static void validateProduct(Product product) throws ProductCollectionException {
        if (product.getName()!= null) {
            product.setName(product.getName().trim());
            System.out.println("product name: " + product.getName());
        } else {
            throw new ProductCollectionException("Product name" + ProductCollectionException.NullException());
        }
        // if product.getDescription() is not null, trim, else throw exception
        if (product.getDescription()!= null) {
            product.setDescription(product.getDescription().trim());
            System.out.println("product description: " + product.getDescription());
        } else {
            throw new ProductCollectionException("Product description" + ProductCollectionException.NullException());
        }
        // if product.getPrice() is not null, trim, else throw exception
//        if (product.getUserId()!= null) {
//            product.setUserId(product.getUserId().trim()); // Trim the ID field as well
//            System.out.println("product userId: " + product.getUserId());
//        } else {
//            throw new ProductCollectionException("Product userId" + ProductCollectionException.NullException());
//        }
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Create an instance of the SHA-256 message digest
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Hash the password bytes
        byte[] hashedBytes = digest.digest(password.getBytes());

        // Convert the hashed bytes to a hexadecimal string
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashedBytes) {
            stringBuilder.append(String.format("%02x", b));
        }

        return stringBuilder.toString();
    }
}


