package com.eosugod.tradehub.reservation.service;

import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.repository.ProductRepository;
import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
import com.eosugod.tradehub.reservation.dto.response.ResponseReservationDto;
import com.eosugod.tradehub.reservation.entity.Reservation;
import com.eosugod.tradehub.reservation.mapper.ReservationMapper;
import com.eosugod.tradehub.reservation.repository.ReservationRepository;
import com.eosugod.tradehub.user.entity.Users;
import com.eosugod.tradehub.user.repository.UserRepository;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 예약 생성
    @Transactional
    public ResponseReservationDto createReservation(RequestCreateReservationDto requestDto) {
        // 중복 예약인지 확인
        if(reservationRepository.existsByBuyerIdAndProductId(requestDto.getBuyerId(), requestDto.getProductId())) {
            throw new ExpectedException(ExceptionCode.EXIST_RESERVATION);
        }
        // 존재하는 상품, 유저인지 확인
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));
        Users buyer = userRepository.findById(requestDto.getBuyerId())
                .orElseThrow(() -> new ExpectedException(ExceptionCode.NOT_EXIST_USER));

        Reservation reservation = ReservationMapper.toEntity(requestDto,product, buyer);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationMapper.toResponseDto(savedReservation);
    }
}
