package com.ecommerce.order_service.mapper;

import com.ecommerce.order_service.dto.OrderItemDTO;
import com.ecommerce.order_service.dto.OrderRequestDTO;
import com.ecommerce.order_service.dto.OrderResponseDTO;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toEntity(OrderRequestDTO dto) {
        Order order = new Order();
        order.setUserId(dto.userId());
        List<OrderItem> items = dto.items().stream()
                .map(itemDTO -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(itemDTO.productId());
                    item.setQuantity(itemDTO.quantity());
                    item.setPrice(itemDTO.price());
                    return item;
                }).collect(Collectors.toList());
        order.setItems(items);
        order.setStatus("PENDING");
        order.setTotalPrice(items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum());
        return order;
    }

    public static OrderResponseDTO toDTO(Order order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(i -> new OrderItemDTO(i.getProductId(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());
        return new OrderResponseDTO(
                order.getId(),
                order.getUserId(),
                items,
                order.getTotalPrice(),
                order.getStatus()
        );
    }
}
