package com.gritlab.letsplay.config;

import com.gritlab.letsplay.model.User;
import com.gritlab.letsplay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User adminUser = new User();
        Optional<User> userOptional = userRepository.findByUser("admin@gmail.com");

        if (userOptional.isEmpty()){
            adminUser.setName("Admin");
            adminUser.setEmail("admin@gmail.com");
            adminUser.setRole("ROLE_ADMIN");
            adminUser.setPassword(passwordEncoder.encode("Admin123!"));
            userRepository.save(adminUser);
        }
    }
}
