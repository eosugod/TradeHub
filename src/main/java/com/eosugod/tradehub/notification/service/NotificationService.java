package com.eosugod.tradehub.notification.service;

import com.eosugod.tradehub.notification.entity.Notification;
import com.eosugod.tradehub.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final RedisPublisher redisPublisher;

    public Notification createNotification(Long userId, String message) {
        // 알림 객체 생성
        Notification notification = Notification.builder()
                                                .userId(userId)
                                                .message(message)
                                                .status(Notification.NotificationStatus.UNREAD)
                                                .build();

        // 알림을 DB에 저장
        Notification savedNotification = notificationRepository.save(notification);

        // Redis로 알림 메시지 발행 (userId를 기반으로 채널 생성)
        redisPublisher.publish(notification);

        return savedNotification;
    }
}
