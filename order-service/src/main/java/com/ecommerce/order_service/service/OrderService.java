package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderCreatedEvent;
import com.ecommerce.order_service.dto.OrderItemDTO;
import com.ecommerce.order_service.dto.OrderRequestDTO;
import com.ecommerce.order_service.dto.OrderResponseDTO;
import com.ecommerce.order_service.kafka.OrderEventProducer;
import com.ecommerce.order_service.mapper.OrderMapper;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate, OrderEventProducer orderEventProducer) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.orderEventProducer = orderEventProducer;
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
        // 1. Stok kontrolü için Product Service'e çağrı yap
        dto.items().forEach(item -> {
            String url = "http://localhost:5001/api/v1/products/" + item.productId();
            var product = restTemplate.getForObject(url, Object.class);
            if (product == null) {
                throw new RuntimeException("Product not found: " + item.productId());
            }
            // TODO: product.stock alanı parse edilip item.quantity ile karşılaştırılabilir
        });

        // 2. Sipariş oluştur
        Order order = OrderMapper.toEntity(dto);
        Order saved = orderRepository.save(order);

        // 3. Event publish (Kafka’ya gönder)
        List<OrderItemDTO> items = saved.getItems().stream()
                .map(i -> new OrderItemDTO(i.getProductId(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());

        OrderCreatedEvent event = new OrderCreatedEvent(
                saved.getId(),
                items,
                saved.getStatus()
        );
        orderEventProducer.sendOrderCreatedEvent(event);

        // 4. DTO response döndür
        return OrderMapper.toDTO(saved);
    }

    public OrderResponseDTO updateOrderStatus(UUID id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return OrderMapper.toDTO(updated);
    }

    public void deleteOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }
}
