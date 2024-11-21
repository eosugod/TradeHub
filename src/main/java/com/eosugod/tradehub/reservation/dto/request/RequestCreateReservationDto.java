package com.eosugod.tradehub.reservation.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateReservationDto {
    @NotNull
    private Long productId;
    @NotNull
    private Long buyerId;
    @NotNull
    @Min(value = 1000)
    private BigDecimal price;
    @NotBlank
    private String locationCode;
    @FutureOrPresent
    private LocalDateTime confirmedAt;
}