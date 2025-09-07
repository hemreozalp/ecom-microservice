package com.ecommerce.notification_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void handleOrderEvent(String message) {
        System.out.println("Notification received: " + message);
    }
}
