package com.ecommerce.notification_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void handleOrderEvent(String message) {
        log.info("📥 Received OrderCreatedEvent: {}", message);
    }
}
