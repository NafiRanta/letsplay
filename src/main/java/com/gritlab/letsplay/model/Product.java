package com.gritlab.letsplay.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="products")
public class Product {
    @Id
    private String id;

    @NotBlank(message = "Product name cannot be empty")
    //@NotNull(message = "Product name cannot be null")
    @Field
    private String name;


    @NotBlank(message = "Product description cannot be empty")
   // @NotNull(message = "Product description cannot be null")
    @Field
    private String description;


    @NotNull(message = "Product price cannot be null")
    @DecimalMin(value = "0.0", message = "Product price must be greater than or equal to 0")
    @Field
    private Double price;


    @NotBlank(message = "Product userId cannot be empty")
   // @NotNull(message = "Product price cannot be null")
    @Field
    private String userId;




}
