package com.gritlab.letsplay.config;

import com.gritlab.letsplay.model.User;
import com.gritlab.letsplay.repository.UserRepository;
import com.gritlab.letsplay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User adminUser = new User();
        Optional<User> userOptional = userRepository.findByUser("admin@gmail.com");

        if (userOptional.isEmpty()){
            adminUser.setName("Admin");
            adminUser.setEmail("admin@gmail.com");
            String salt = BCrypt.gensalt(12);
            String hashPassword = FieldValidator.hashPassword("Admin123!");
            adminUser.setRole("ROLE_ADMIN");
            adminUser.setPassword(salt+hashPassword); // You should hash the password before setting it
            userRepository.save(adminUser);
        }

    }
}
