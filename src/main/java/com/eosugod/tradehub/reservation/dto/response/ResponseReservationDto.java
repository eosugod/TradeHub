package com.eosugod.tradehub.reservation.dto.response;

import com.eosugod.tradehub.reservation.entity.Reservation;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseReservationDto {
    private Long id;
    private Long productId;
    private Long sellerId;
    private Long buyerId;
    private BigDecimal price;
    private String locationCode;
    private String confirmedAt;
    private Reservation.ReservationState state;
}
