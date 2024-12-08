package com.eosugod.tradehub.reservation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateReservationDto {
    @NotBlank @Size(min=10, max=10)
    private String locationCode;
    @FutureOrPresent
    private LocalDateTime confirmedAt;
}
