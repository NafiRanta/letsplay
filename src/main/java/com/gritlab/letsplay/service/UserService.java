package com.gritlab.letsplay.service;

import com.gritlab.letsplay.exception.UserCollectionException;
import com.gritlab.letsplay.model.User;

import javax.validation.ConstraintViolationException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {
    public void createUser(User user, String url) throws ConstraintViolationException, UserCollectionException, NoSuchAlgorithmException;

    public List<User> getAllUsers();

    public User getSingleUser(String id) throws UserCollectionException;

    public void updateUser(String id, User user, String url) throws UserCollectionException, NoSuchAlgorithmException;

    public void deleteUserById(String id) throws UserCollectionException;


}
