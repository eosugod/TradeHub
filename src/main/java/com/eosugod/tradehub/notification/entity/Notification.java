package com.eosugod.tradehub.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"@class"})
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId; // 알림을 받을 사용자 ID
    private String message; // 알림 메시지 내용
    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.UNREAD;

    public enum NotificationStatus {
        READ, // 읽음
        UNREAD // 미읽음
    }
}
