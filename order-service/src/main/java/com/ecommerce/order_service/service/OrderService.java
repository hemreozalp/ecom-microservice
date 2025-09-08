package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.*;
import com.ecommerce.order_service.kafka.OrderEventProducer;
import com.ecommerce.order_service.mapper.OrderMapper;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final OrderEventProducer orderEventProducer;
    private final Counter ordersCreatedCounter;
    private final Counter ordersFailedCounter;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate, OrderEventProducer orderEventProducer, MeterRegistry meterRegistry) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.orderEventProducer = orderEventProducer;
        this.ordersCreatedCounter = meterRegistry.counter("orders_created_total");
        this.ordersFailedCounter = meterRegistry.counter("orders_failed_total");
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return OrderMapper.toDTO(order);
    }

    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        try {
            dto.items().forEach(item -> {
                String url = "http://product-service:8080/api/v1/products/" + item.productId();
                ProductResponseDTO product = restTemplate.getForObject(url, ProductResponseDTO.class);
                if (product == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Product not found: " + item.productId());
                }
                if (product.stock() < item.quantity()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Insufficient stock for product: " + product.name());
                }
            });
        } catch (RuntimeException ex) {
            ordersFailedCounter.increment();
            throw ex;
        }

        Order order = OrderMapper.toEntity(dto);
        order.setStatus("PENDING"); // default status
        Order saved = orderRepository.save(order);

        List<OrderItemDTO> itemsDTO = saved.getItems().stream()
                .map(i -> new OrderItemDTO(i.getProductId(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());

        OrderCreatedEvent event = new OrderCreatedEvent(
                saved.getUserId(),
                saved.getId(),
                itemsDTO,
                saved.getStatus()
        );
        orderEventProducer.sendOrderCreatedEvent(event);
        ordersCreatedCounter.increment();

        return OrderMapper.toDTO(saved);
    }


    public OrderResponseDTO updateOrderStatus(UUID id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        // Status validation
        if (!List.of("PENDING", "COMPLETED", "CANCELED").contains(status.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status");
        }

        order.setStatus(status.toUpperCase());
        Order updated = orderRepository.save(order);
        return OrderMapper.toDTO(updated);
    }


    public void deleteOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }
}
