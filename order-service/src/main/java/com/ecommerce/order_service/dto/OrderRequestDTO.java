package com.ecommerce.order_service.dto;

import java.util.List;
import java.util.UUID;

public record OrderRequestDTO(UUID userId,
                              List<OrderItemDTO> items) {
}
