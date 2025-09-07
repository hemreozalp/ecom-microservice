package com.ecommerce.product_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
