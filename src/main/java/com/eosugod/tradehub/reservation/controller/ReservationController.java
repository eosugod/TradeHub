package com.eosugod.tradehub.reservation.controller;

import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
import com.eosugod.tradehub.reservation.dto.response.ResponseReservationDto;
import com.eosugod.tradehub.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products/{productId}/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    // 예약 생성
    @PostMapping
    public ResponseEntity<ResponseReservationDto> createReservation(@Valid @RequestBody RequestCreateReservationDto requestDto) {
        ResponseReservationDto createdReservation = reservationService.createReservation(requestDto);
        return ResponseEntity.ok(createdReservation);
    }
}
