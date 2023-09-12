package com.gritlab.letsplay.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="users")

public class User {
    @Id
    private String id;


    @NotBlank(message = "User name cannot be empty")
   // @NotNull(message = "User name cannot be null")
    @Field
    private String name;


    @NotBlank(message = "User email cannot be empty")
   // @NotNull(message = "User email cannot be null")
    @Field
    @Email(message="Invalid email format")
    private String email;


    @NotBlank(message = "User password cannot be empty")
   // @NotNull(message = "User password cannot be null")
    @Field
    @Size(min = 4, max = 1000000000, message = "password must be between 4 and 50 characters")
    private String password;


    //@NotNull(message = "User role cannot be null")
    @Field
    private String role;

    public String uuidGenerator() {
        // Implement logic to generate a unique product ID, e.g., using UUID
        return UUID.randomUUID().toString();
    }

}
