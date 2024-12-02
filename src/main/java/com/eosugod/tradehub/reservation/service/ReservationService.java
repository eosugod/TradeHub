package com.eosugod.tradehub.reservation.service;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.product.port.ProductPort;
import com.eosugod.tradehub.product.repository.ProductRepository;
import com.eosugod.tradehub.reservation.domain.ReservationDomain;
import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
import com.eosugod.tradehub.reservation.dto.response.ResponseReservationDto;
import com.eosugod.tradehub.reservation.entity.Reservation;
import com.eosugod.tradehub.reservation.mapper.ReservationMapper;
import com.eosugod.tradehub.reservation.port.ReservationPort;
import com.eosugod.tradehub.reservation.repository.ReservationRepository;
import com.eosugod.tradehub.user.domain.UserDomain;
import com.eosugod.tradehub.user.entity.Users;
import com.eosugod.tradehub.user.mapper.UserMapper;
import com.eosugod.tradehub.user.port.UserPort;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationPort reservationPort;
    private final ProductPort productPort;
    private final UserPort userPort;

    // 예약 생성
    @Transactional
    public ResponseReservationDto createReservation(RequestCreateReservationDto dto) {
        UserDomain userDomain = userPort.read(dto.getBuyerId());
        Users user = UserMapper.domainToPersistence(userDomain);

        ProductDomain productDomain = productPort.findById(dto.getProductId())
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));
        Product product = ProductMapper.domainToPersistence(productDomain);

        // 중복 확인
        if(reservationPort.existsByProductIdAndBuyerId(dto.getProductId(), dto.getBuyerId())) {
            throw new ExpectedException(ExceptionCode.EXIST_RESERVATION);
        }

        // 판매중 확인
        if(!productDomain.getState().equals(Product.SaleState.FOR_SALE)) {
            throw new ExpectedException(ExceptionCode.PRODUCT_NOT_FOR_SALE);
        }

        // 잔액 확인
        if(userDomain.getCash().getValue().compareTo(dto.getPrice()) < 0) {
            throw new ExpectedException(ExceptionCode.NOT_ENOUGH_POINT);
        }

        Reservation reservation = ReservationMapper.dtoToPersistence(dto, product, user);
        ReservationDomain reservationDomain = ReservationMapper.persistenceToDomain(reservation);

        return ReservationMapper.domainToDto(reservationPort.save(reservationDomain, product, user));
    }

    // 단일 예약 조회
    @Transactional(readOnly = true)
    public ResponseReservationDto getReservation(Long id) {
        ReservationDomain reservationDomain = reservationPort.findById(id)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.RESERVATION_NOT_FOUND));
        return ReservationMapper.domainToDto(reservationDomain);
    }
}
