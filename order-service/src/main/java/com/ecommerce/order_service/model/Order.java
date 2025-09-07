package com.ecommerce.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private UUID id;
    private UUID userId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;
    private double totalPrice;
    private String status; // PENDING, COMPLETED, CANCELED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
