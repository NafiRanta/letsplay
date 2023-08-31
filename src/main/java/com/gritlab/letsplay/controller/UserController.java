package com.gritlab.letsplay.controller;

import com.gritlab.letsplay.exception.UserCollectionException;
import com.gritlab.letsplay.model.User;
import com.gritlab.letsplay.repository.UserRepository;
import com.gritlab.letsplay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(){
        List<User> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, userList.size() > 0 ? HttpStatus.OK: HttpStatus.NOT_FOUND);
    }

    @PostMapping("/users/new")
    public ResponseEntity<?> createUser(@RequestBody User user){
        try {
            System.out.println("user " + user);
            userService.createUser(user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            System.out.println("ConstraintViolationException " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (UserCollectionException e){
            System.out.println("UserCollectionException " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/users/single/{id}")
    public ResponseEntity<?> getSingleUser(
            @PathVariable("id") String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            User user = userService.getSingleUser(id);
            // Check if the authenticated user's username matches the requested user's id
            if (userDetails.getUsername().equals(user.getEmail()) ||
                    userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ){
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/users/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateUserById(@PathVariable("id") String id, @RequestBody User user){
       try{
           userService.updateUser(id, user);
           return new ResponseEntity<>("Update User with id " + id, HttpStatus.OK);
       } catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
       } catch (Exception e){
           return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
       }
    }

    @DeleteMapping("/users/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") String id){
        try{
            userService.deleteUserById(id);
            return new ResponseEntity<>("Successfully deleted user with id " + id, HttpStatus.OK);
        } catch (UserCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
