package com.ecommerce.product_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductResponseDTO(
        @NotNull UUID id,
        String name,
        String description,
        double price,
        int stock) {
}
