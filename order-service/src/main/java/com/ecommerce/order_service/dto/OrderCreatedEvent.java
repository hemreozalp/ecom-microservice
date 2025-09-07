package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.model.OrderItem;

import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(UUID orderId,
                                List<OrderItemDTO> items,
                                String status) {
}
