package com.ecommerce.product_service.dto;

import java.util.List;
import java.util.UUID;

public record ProductServiceOrderCreatedEvent(
        UUID orderId,
        List<ProductServiceOrderItemDTO> items,
        String status
) {}