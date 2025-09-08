package com.ecommerce.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        @NotNull UUID userId,
        @NotNull UUID orderId,
        List<OrderItemDTO> items,
        @NotBlank String status) {
}
