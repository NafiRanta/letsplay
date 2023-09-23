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
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void validateUser(User user, String url) throws UserCollectionException {
        if (user.getName()!= null) {
            user.setName(removeExtraSpaces(user.getName()).trim());
        } else {
            throw new UserCollectionException("User name" + UserCollectionException.NullException());
        }

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
            if (user.getPassword().length() < 4) {
                throw new UserCollectionException("User password" + UserCollectionException.PasswordLengthException());
            } else if (user.getPassword().contains(" ")){
                throw new UserCollectionException(UserCollectionException.InvalidPassword());
            }
            user.setPassword(user.getPassword().trim()); // Trim the ID field as well
        } else {
            throw new UserCollectionException("User password" + UserCollectionException.NullException());
        }

        if (user.getRole()!= null && url.equals("update") ){
            if (!(user.getRole().trim().equals("ROLE_ADMIN")  || user.getRole().trim().equals("ROLE_USER"))) {
                throw new UserCollectionException("User role" + UserCollectionException.InvalidRoleException());
            }
            user.setRole(user.getRole().trim());
        }

    }

    public static void validateProduct(Product product) throws ProductCollectionException {
        if (product.getName()!= null) {
            product.setName(removeExtraSpaces(product.getName()).trim());
            System.out.println("product name: " + product.getName());
        } else {
            throw new ProductCollectionException("Product name" + ProductCollectionException.NullException());
        }

        if (product.getDescription()!= null) {
            product.setDescription(removeExtraSpaces(product.getDescription()).trim());
            System.out.println("product description: " + product.getDescription());
        } else {
            throw new ProductCollectionException("Product description" + ProductCollectionException.NullException());
        }
    }


    private static String removeExtraSpaces(String value) {
        return value.replaceAll("\s+", " ").trim();
    }
}


