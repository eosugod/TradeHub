package com.eosugod.tradehub.notification.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateNotificationDto {
    private Long userId;
    private String message;
}
