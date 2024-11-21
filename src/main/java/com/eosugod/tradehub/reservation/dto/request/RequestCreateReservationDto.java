package com.eosugod.tradehub.reservation.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateReservationDto {
    private Long productId;
    private Long buyerId;
    private BigDecimal price;
    private String locationCode;
    private LocalDateTime confirmedAt;
}