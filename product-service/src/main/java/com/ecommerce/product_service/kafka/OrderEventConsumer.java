package com.ecommerce.product_service.kafka;

import com.ecommerce.product_service.dto.OrderCreatedEvent;
import com.ecommerce.product_service.dto.OrderItemDTO;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderEventConsumer {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public OrderEventConsumer(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-events", groupId = "product-group")
    public void handleOrderCreated(String message) throws Exception {
        OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);
        for (OrderItemDTO item : event.items()) {
            UUID productId = item.productId();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
            int newStock = product.getStock() - item.quantity();
            if (newStock < 0) {
                // Out-of-sync safety; do not allow negative stock
                newStock = 0;
            }
            product.setStock(newStock);
            productRepository.save(product);
        }
        System.out.println("[PRODUCT] Stock decremented for order " + event.orderId());
    }
}


