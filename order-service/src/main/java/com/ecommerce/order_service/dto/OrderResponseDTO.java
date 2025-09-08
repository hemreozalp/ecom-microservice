package com.ecommerce.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(

        @NotNull UUID id,
        @NotNull UUID userId,
        List<OrderItemDTO> items,
        @Positive double totalPrice,
        @NotBlank String status) {
}
