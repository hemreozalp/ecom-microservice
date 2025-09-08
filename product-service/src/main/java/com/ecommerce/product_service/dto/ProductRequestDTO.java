package com.ecommerce.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
        @NotBlank(message = "Product name is required")
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,

        @Size(max = 500, message = "Description must be at most 500 characters")
        String description,

        @Positive(message = "Price must be greater than 0")
        double price,

        @PositiveOrZero(message = "Stock cannot be negative")
        int stock) {
}
