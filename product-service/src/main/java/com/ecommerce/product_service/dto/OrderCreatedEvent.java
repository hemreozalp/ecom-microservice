package com.ecommerce.product_service.dto;

import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(UUID userId, UUID orderId, List<OrderItemDTO> items, String status) {}


