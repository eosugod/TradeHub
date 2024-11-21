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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 예약 생성
    @Transactional
    public ResponseReservationDto createReservation(RequestCreateReservationDto requestDto) {
        // 존재하는 상품, 유저인지 확인
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));
        Users buyer = userRepository.findById(requestDto.getBuyerId())
                .orElseThrow(() -> new ExpectedException(ExceptionCode.NOT_EXIST_USER));

        // 중복 예약인지 확인
        if(reservationRepository.existsByBuyerIdAndProductId(requestDto.getBuyerId(), requestDto.getProductId())) {
            throw new ExpectedException(ExceptionCode.EXIST_RESERVATION);
        }

        // 상품 상태가 판매중인지 확인
        if(!product.getState().equals(Product.SaleState.FOR_SALE)) {
            throw new ExpectedException(ExceptionCode.PRODUCT_NOT_FOR_SALE);
        }

        // 구매자의 포인트가 예약 가격 이상인지 확인
        if(buyer.getCash().getValue().compareTo(requestDto.getPrice()) < 0) {
            throw new ExpectedException(ExceptionCode.NOT_ENOUGH_POINT);
        }

        Reservation reservation = ReservationMapper.toEntity(requestDto, product, buyer);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationMapper.toResponseDto(savedReservation);
    }

    // 단일 예약 조회
    @Transactional(readOnly = true)
    public ResponseReservationDto getReservation(Long reservaitonId) {
        Reservation reservation = reservationRepository.findById(reservaitonId)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.RESERVATION_NOT_FOUND));
        return ReservationMapper.toResponseDto(reservation);
    }

    // 해당 상품에 걸린 전체 예약 조회
    @Transactional(readOnly = true)
    public Page<ResponseReservationDto> getAllReservations(Long productId, int page, int size) {
        if(!productRepository.existsById(productId)) {
            throw new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Reservation> reservations = reservationRepository.findAllByProductId(productId, pageable);
        return reservations.map(ReservationMapper::toResponseDto);
    }
}
