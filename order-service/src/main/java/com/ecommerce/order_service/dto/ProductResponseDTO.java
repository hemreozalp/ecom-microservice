package com.ecommerce.order_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductResponseDTO(
        @NotNull UUID id,
        String name,
        int stock) { }
