package com.ecommerce.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderRequestDTO(
        @NotNull UUID userId,

        @NotEmpty(message = "Order must contain at least one item")
        List<@Valid OrderItemDTO> items) {
}
