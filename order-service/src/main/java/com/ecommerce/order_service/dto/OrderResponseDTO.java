package com.ecommerce.order_service.dto;

import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(UUID id,
                               UUID userId,
                               List<OrderItemDTO> items,
                               double totalPrice,
                               String status) {
}
