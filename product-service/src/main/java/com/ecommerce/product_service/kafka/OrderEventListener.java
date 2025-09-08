package com.ecommerce.product_service.kafka;

import com.ecommerce.product_service.dto.ProductServiceOrderCreatedEvent;
import com.ecommerce.product_service.dto.ProductServiceOrderItemDTO;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEvenetListener {

    private final ProductRepository productRepository;

    public OrderEvenetListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "order-topic", groupId = "product-group")
    public void handleOrderCreated(ProductServiceOrderCreatedEvent event) {

        for (ProductServiceOrderItemDTO item: event.items()) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.productId()));

            int newStock = product.getStock() - item.quantity();
            if (newStock < 0) {
                throw new RuntimeException("Insufficient stock for product: " + product.getId());
            }

            product.setStock(newStock);
            productRepository.save(product);
        }
    }
}
