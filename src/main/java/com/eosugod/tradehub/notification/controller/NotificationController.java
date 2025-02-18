package com.eosugod.tradehub.notification.controller;

import com.eosugod.tradehub.notification.dto.RequestCreateNotificationDto;
import com.eosugod.tradehub.notification.service.NotificationService;
import com.eosugod.tradehub.notification.service.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final SseEmitterManager sseEmitterManager;
    private final NotificationService notificationService;

    @GetMapping("/connect/{userId}")
    public SseEmitter connect(@PathVariable Long userId) {
        return sseEmitterManager.connect(userId);
    }

    @PostMapping("/send")
    public void sendNotification(@RequestBody RequestCreateNotificationDto dto) {
        // 알림을 생성하고 DB에 저장한 후 Redis로 발행
        notificationService.createNotification(dto.getUserId(), dto.getMessage());
    }
}
