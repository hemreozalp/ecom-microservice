package com.ecommerce.product_service.dto;

import java.util.UUID;

public record OrderItemDTO(UUID productId, int quantity, double price) {}


