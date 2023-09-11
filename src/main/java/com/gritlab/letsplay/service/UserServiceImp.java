package com.gritlab.letsplay.service;

import com.gritlab.letsplay.config.FieldValidator;
import com.gritlab.letsplay.exception.ProductCollectionException;
import com.gritlab.letsplay.exception.UserCollectionException;
import com.gritlab.letsplay.model.Product;
import com.gritlab.letsplay.model.User;
import com.gritlab.letsplay.model.UserDTO;
import com.gritlab.letsplay.repository.ProductRepository;
import com.gritlab.letsplay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(User user) throws ConstraintViolationException, UserCollectionException, NoSuchAlgorithmException {
        FieldValidator.validateUser(user);
        Optional<User> userOptional = userRepository.findByUser(user.getEmail());
        if(userOptional.isPresent()){
           throw new UserCollectionException(UserCollectionException.UserAlreadyExistException());
        } else{
            String salt = BCrypt.gensalt(12);
            // hash user.getPassWord();
            String password = user.getPassword();
            String hashPassword = FieldValidator.hashPassword(user.getPassword());
            String hashedSaltedPassword = salt + hashPassword;
            user.setPassword(hashedSaltedPassword);
            userRepository.save(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.size() > 0){
            return users;
        } else{
            return new ArrayList<User>();
        }
    }

    @Override
    public User getSingleUser(String id) throws UserCollectionException {
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            throw new UserCollectionException(UserCollectionException.NotFoundException(id));
        } else{
            return userOptional.get();
        }
    }

    @Override
    public void updateUser(String id, User user) throws UserCollectionException {
        FieldValidator.validateUser(user);
        Optional<User> userOptional = userRepository.findById(id);
        System.out.println("userOptional update user" + userOptional);
        Optional<User> userOptionalSameName = userRepository.findByUser(user.getEmail());
        System.out.println("userOptionalSameName" + userOptionalSameName);

        if (userOptional.isPresent()) {
          if (userOptionalSameName.isPresent() && !userOptionalSameName.get().getId().equals(id)){
                throw new UserCollectionException(UserCollectionException.UserAlreadyExistException());
            }
           User userUpdate = userOptional.get();
           userUpdate.setName(user.getName());
           userUpdate.setEmail(user.getEmail());
           userUpdate.setPassword(user.getPassword());
           userUpdate.setRole(user.getRole());
           userRepository.save(userUpdate);
        } else {
            throw new UserCollectionException(UserCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void deleteUserById(String id) throws UserCollectionException {
        Optional<User> userOptional = userRepository.findById(id);
        System.out.println("userOptional: "+ userOptional);

        if (!userOptional.isPresent()){
            throw new UserCollectionException(UserCollectionException.NotFoundException(id));
        } else {
            // Find all products associated with the user and delete them
            List<Product> productsToDelete = productRepository.findByUserId(id);
            System.out.println("Products to delete: " + productsToDelete);
            for (Product product : productsToDelete) {
                productRepository.delete(product);
            }

            // Now delete the user
            userRepository.deleteById(id);

        }
    }

}
