package com.eosugod.tradehub.notification.service;

import com.eosugod.tradehub.notification.entity.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSubscriber {
    private final SseEmitterManager sseEmitterManager;
    private final ObjectMapper objectMapper;
    // Redis 메시지 수신 메서드
    public void handleMessage(String message) {
        try {
            // 메시지를 JSON으로 파싱
            Notification notification = objectMapper.readValue(message, Notification.class);
            Long userId = notification.getUserId();

            // 해당 유저에게 메시지 전송
            sseEmitterManager.broadcast(userId, message);

        } catch (Exception e) {
            System.err.println("Failed to extract userId from message: " + message);
            e.printStackTrace();
        }
    }
}
