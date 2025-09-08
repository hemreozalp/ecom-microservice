package com.ecommerce.notification_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void onOrderCreated(String message) {
        // Simple console simulation
        System.out.println("[EMAIL] Yeni sipariş alındı: " + message);
        System.out.println("[SMS] Yeni sipariş alındı: " + message);
    }
}


