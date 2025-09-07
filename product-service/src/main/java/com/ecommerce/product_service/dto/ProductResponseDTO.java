package com.ecommerce.product_service.dto;

import java.util.UUID;

public record ProductResponseDTO(UUID id,
                                 String name,
                                 String description,
                                 double price,
                                 int stock) {
}
