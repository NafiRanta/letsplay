package com.gritlab.letsplay.controller;

import com.gritlab.letsplay.config.FieldValidator;
import com.gritlab.letsplay.exception.UserCollectionException;
import com.gritlab.letsplay.model.AuthRequest;
import com.gritlab.letsplay.model.User;
import com.gritlab.letsplay.model.UserDTO;
import com.gritlab.letsplay.repository.UserRepository;
import com.gritlab.letsplay.service.JwtService;
import com.gritlab.letsplay.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.gritlab.letsplay.config.FieldValidator.hashPassword;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/users/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest, HttpServletRequest request) throws NoSuchAlgorithmException, UserCollectionException {
        Optional<User> userOptional = userRepository.findByUser(authRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String storedPassword = user.getPassword(); // This should contain the stored salted and hashed password
            String passwordToCheck = storedPassword.replaceFirst("^\\$2a\\$10\\$", "") ;
            System.out.println("passwordToCheck : " + passwordToCheck);


            // Compare the stored salted and hashed password with the newly generated hash
            if (passwordToCheck.equals(storedPassword)) {
                return jwtService.generateToken(authRequest.getUsername());
            } else {
                throw new BadCredentialsException("Invalid password");
            }
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }


    @GetMapping("/users/all")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<?> getAllUsers(){
        try {
            List<User> userList = userService.getAllUsers();
            List<UserDTO> userDTOList = userList.stream()
                    .map(user -> new UserDTO(user.getId(), user.getName(), user.getRole()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(userDTOList, HttpStatus.OK);
        } catch (Exception e){
            System.out.println("e.getmessage: " + e.getMessage());
            return new ResponseEntity<>("Authentication required", HttpStatus.BAD_REQUEST);
        }


       // return new ResponseEntity<>(userList, userList.size() > 0 ? HttpStatus.OK: HttpStatus.NOT_FOUND);
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
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<?> getSingleUser(
            @PathVariable("id") String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            User user = userService.getSingleUser(id);
            UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getRole());
            // Check if the authenticated user's username matches the requested user's id
            if (userDetails.getUsername().equals(user.getEmail()) ||
                    userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ){
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/users/update/{id}")
    @Secured({"ROLE_ADMIN"})
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
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> deleteUserById(@PathVariable("id") String id){
        try{
            userService.deleteUserById(id);
            return new ResponseEntity<>("Successfully deleted user with id " + id, HttpStatus.OK);
        } catch (UserCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
