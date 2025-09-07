package com.ecommerce.product_service.dto;

public record ProductRequestDTO(String name,
                                String description,
                                double price,
                                int stock) {
}
