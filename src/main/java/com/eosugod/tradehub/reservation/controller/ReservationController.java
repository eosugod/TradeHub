package com.eosugod.tradehub.reservation.controller;

import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
import com.eosugod.tradehub.reservation.dto.request.RequestUpdateReservationDto;
import com.eosugod.tradehub.reservation.dto.response.ResponseReservationDto;
import com.eosugod.tradehub.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    // 예약 생성
    @PostMapping
    public ResponseEntity<ResponseReservationDto> createReservation(@Valid @RequestBody RequestCreateReservationDto requestDto) {
        ResponseReservationDto createdReservation = reservationService.createReservation(requestDto);
        return ResponseEntity.ok(createdReservation);
    }

    // 단일 예약 조회
    @GetMapping("/{reservationId}")
    public ResponseEntity<ResponseReservationDto> getReservation(@PathVariable Long reservationId) {
        ResponseReservationDto responseDto = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(responseDto);
    }

    // 해당 상품에 걸린 전체 예약 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<Page<ResponseReservationDto>> getAllReservations(@PathVariable Long productId,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "20") int size) {
        Page<ResponseReservationDto> responseDtos = reservationService.getAllReservations(productId, page, size);
        return ResponseEntity.ok(responseDtos);
    }

    // 해당 유저의 확정된 단일 예약 조회
    @GetMapping("/confirmed/{userId}/{reservationId}")
    public ResponseEntity<ResponseReservationDto> getConfirmedReservation(
            @PathVariable Long userId,
            @PathVariable Long reservationId) {
        ResponseReservationDto responseDto = reservationService.getConfirmedReservation(reservationId, userId);
        return ResponseEntity.ok(responseDto);
    }

    // 해당 유저의 확정된 모든 예약 조회
    @GetMapping("/confirmed/{userId}")
    public ResponseEntity<Page<ResponseReservationDto>> getAllConfirmedReservations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ResponseReservationDto> responseDtos = reservationService.getAllConfirmedReservations(userId, page, size);
        return ResponseEntity.ok(responseDtos);
    }

    // 예약 확정
    @PostMapping("/{reservationId}/confirm/{userId}")
    public ResponseReservationDto confirmReservation(@PathVariable Long reservationId,
                                                     @PathVariable Long userId) {
        return reservationService.confirmReservation(reservationId, userId);
    }

    // 예약 수정
    @PutMapping("/update/{reservationId}/{userId}")
    public ResponseEntity<ResponseReservationDto> updateReservation(@PathVariable Long reservationId,
                                                                         @PathVariable Long userId,
                                                                         @RequestBody RequestUpdateReservationDto dto) {
        ResponseReservationDto responseDto = reservationService.updateReservation(reservationId, userId, dto);
        return ResponseEntity.ok(responseDto);
    }

    // 예약 취소
    @PatchMapping("/{reservationId}/{userId}")
    public ResponseEntity<ResponseReservationDto> cancelReservation(@PathVariable Long reservationId,
                                                                    @PathVariable Long userId) {
        ResponseReservationDto responseDto = reservationService.cancelReservation(reservationId, userId);
        return ResponseEntity.ok(responseDto);
    }

    // 거래 완료 요청 (판매자, 구매자)
    @PatchMapping("/{reservationId}/complete/{userId}")
    public ResponseEntity<ResponseReservationDto> completeReservation(@PathVariable Long reservationId,
                                                                      @PathVariable Long userId) {
        ResponseReservationDto responseDto = reservationService.completeReservation(reservationId, userId);
        return ResponseEntity.ok(responseDto);
    }
}
