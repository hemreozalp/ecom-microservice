package com.ecommerce.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record OrderItemDTO(

        @NotNull UUID productId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @Positive(message = "Price must be greater than 0")
        double price) {
}
