package com.eosugod.tradehub.notification.service;

import com.eosugod.tradehub.notification.entity.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publish(Notification notification) {
        try {
            String message = objectMapper.writeValueAsString(notification);
            redisTemplate.convertAndSend("notifications", message);
        } catch (Exception e) {
            System.err.println("Failed to publish notification: " + notification);
            e.printStackTrace();
        }
    }
}
