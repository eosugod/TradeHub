package com.eosugod.tradehub.reservation.service;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.product.port.ProductPort;
import com.eosugod.tradehub.reservation.domain.ReservationDomain;
import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
import com.eosugod.tradehub.reservation.dto.request.RequestUpdateReservationDto;
import com.eosugod.tradehub.reservation.dto.response.ResponseReservationDto;
import com.eosugod.tradehub.reservation.entity.Reservation;
import com.eosugod.tradehub.reservation.mapper.ReservationMapper;
import com.eosugod.tradehub.reservation.port.ReservationPort;
import com.eosugod.tradehub.user.domain.UserDomain;
import com.eosugod.tradehub.user.entity.Users;
import com.eosugod.tradehub.user.mapper.UserMapper;
import com.eosugod.tradehub.user.port.UserPort;
import com.eosugod.tradehub.vo.Money;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        // 판매자와 구매자가 같은지 확인
        if(productDomain.getSellerId().equals(dto.getBuyerId())) {
            throw new ExpectedException(ExceptionCode.RESERVATION_INVALID_BUYER);
        }

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

    // 해당 상품에 걸린 전체 예약 조회
    @Transactional(readOnly = true)
    public Page<ResponseReservationDto> getAllReservations(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationDomain> reservationDomainPage = reservationPort.findByProductId(productId, pageable);
        return reservationDomainPage.map(ReservationMapper::domainToDto);
    }

    // 해당 유저의 확정된 단일 예약 조회
    public ResponseReservationDto getConfirmedReservation(Long reservationId, Long userId) {
        ReservationDomain reservation = reservationPort.findById(reservationId)
                                                       .filter(res -> (res.getBuyer().getId().equals(userId) || res.getProduct().getSellerId().equals(userId))
                                                               && res.getState() == Reservation.ReservationState.CONFIRMED)
                                                       .orElseThrow(() -> new ExpectedException(ExceptionCode.RESERVATION_NOT_FOUND));

        return ReservationMapper.domainToDto(reservation);
    }

    // 해당 유저의 확정된 모든 예약 조회
    public Page<ResponseReservationDto> getAllConfirmedReservations(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationDomain> buyerReservations = reservationPort.findAllByBuyerIdAndState(userId, Reservation.ReservationState.CONFIRMED, pageable);
        Page<ReservationDomain> sellerReservations = reservationPort.findAllByProduct_SellerIdAndState(userId, Reservation.ReservationState.CONFIRMED, pageable);

        List<ReservationDomain> allReservations = new ArrayList<>();
        allReservations.addAll(buyerReservations.getContent());
        allReservations.addAll(sellerReservations.getContent());

        List<ResponseReservationDto> responseReservationDtos = allReservations.stream()
                                                                              .map(reservation -> ReservationMapper.domainToDto(reservation))
                                                                              .collect(Collectors.toList());
        long totalElements = buyerReservations.getTotalElements() + sellerReservations.getTotalElements();

        return new PageImpl<>(responseReservationDtos, pageable, totalElements);
    }

    // 예약 확정
    @Transactional
    public ResponseReservationDto confirmReservation(Long id, Long userId) {
        // 예약 확인
        ReservationDomain reservationDomain = reservationPort.findById(id)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.RESERVATION_NOT_FOUND));

        // 판매자의 요청인지 확인
        if(!Objects.equals(userId, reservationDomain.getProduct().getSellerId())) {
            throw new ExpectedException(ExceptionCode.UNAUTHORIZED_ACTION);
        }

        // 판매중 확인
        ProductDomain productDomain = productPort.findById(reservationDomain.getProduct().getId())
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));

        if(productDomain.getState() != Product.SaleState.FOR_SALE) {
            throw new ExpectedException(ExceptionCode.PRODUCT_NOT_FOR_SALE);
        }

        // 구매자 잔액 확인
        UserDomain userDomain = userPort.read(reservationDomain.getBuyer().getId());
        if(userDomain.getCash().getValue().compareTo(reservationDomain.getPrice().getValue()) < 0) {
            throw new ExpectedException(ExceptionCode.NOT_ENOUGH_POINT);
        }

        // 구매자의 포인트 차감
        Money updatedCash = userDomain.getCash().subtract(reservationDomain.getPrice());
        UserDomain updatedUserDomain = userDomain.updatedCash(updatedCash);
        userPort.save(updatedUserDomain);

        // 상품 상태 판매중 -> 예약중 변경 & 상품 buyerId 등록
        ProductDomain updatedProductDomain = productDomain.updatedState(Product.SaleState.RESERVED);
        productPort.save(updatedProductDomain);


        // 예약 상태 예약중 -> 확정됨 변경
        ReservationDomain confirmedReservationDomain = reservationDomain.updateState(Reservation.ReservationState.CONFIRMED); // 상태 변경 메서드 호출
        reservationPort.save(confirmedReservationDomain,
                ProductMapper.domainToPersistence(updatedProductDomain),
                UserMapper.domainToPersistence(updatedUserDomain));

        return ReservationMapper.domainToDto(confirmedReservationDomain);
    }

    // 예약 수정
    @Transactional
    public ResponseReservationDto updateReservation(Long id, Long userId, RequestUpdateReservationDto dto) {
        ReservationDomain reservationDomain = reservationPort.findById(id)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.RESERVATION_NOT_FOUND));

        if(reservationDomain.getState() != Reservation.ReservationState.PENDING) {
            throw new ExpectedException(ExceptionCode.RESERVATION_NOT_PENDING);
        }

        if(!reservationDomain.getBuyer().getId().equals(userId) && !reservationDomain.getProduct().getSellerId().equals(userId)) {
            throw new ExpectedException(ExceptionCode.UNAUTHORIZED_ACTION);
        }

        ReservationDomain updatedReservationDomain = reservationDomain.update(dto);
        return ReservationMapper.domainToDto(reservationPort.save(updatedReservationDomain, ProductMapper.domainToPersistence(reservationDomain.getProduct()), UserMapper.domainToPersistence(reservationDomain.getBuyer())));
    }

    // 예약 취소
    @Transactional
    public ResponseReservationDto cancelReservation(Long id, Long sellerId) {
        // 예약 조회
        ReservationDomain reservationDomain = reservationPort.findById(id)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.RESERVATION_NOT_FOUND));

        // 상품의 판매자인지 확인
        if(!reservationDomain.getProduct().getSellerId().equals(sellerId)) {
            throw new ExpectedException(ExceptionCode.UNAUTHORIZED_ACTION);
        }

        // 예약 상태 확인
        if(reservationDomain.getState() != Reservation.ReservationState.CONFIRMED) {
            throw new ExpectedException(ExceptionCode.RESERVATION_NOT_CONFIRMED);
        }

        // 상품 상태 예약중 -> 판매중 변경
        ProductDomain productDomain = reservationDomain.getProduct();
        ProductDomain updatedProductDomain = productDomain.updatedState(Product.SaleState.RESERVED);
        productPort.save(updatedProductDomain);

        // 구매자 포인트 복원
        UserDomain userDomain = reservationDomain.getBuyer();
        Money updatedCash = userDomain.getCash().add(reservationDomain.getPrice());
        UserDomain updatedUserDomain = userDomain.updatedCash(updatedCash);
        userPort.save(updatedUserDomain);

        // 예약 상태 예약중 -> 취소됨으로 변경
        ReservationDomain canceledReservationDomain = reservationDomain.cancel();
        reservationPort.save(canceledReservationDomain,
                ProductMapper.domainToPersistence(updatedProductDomain),
                UserMapper.domainToPersistence(updatedUserDomain));

        return ReservationMapper.domainToDto(canceledReservationDomain);
    }

    // 거래 완료 요청
    @Transactional
    public ResponseReservationDto completeReservation(Long id, Long userId) {
        // 예약 조회
        ReservationDomain reservationDomain = reservationPort.findById(id)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.RESERVATION_NOT_FOUND));

        // 중복 요청 확인
        if(reservationDomain.getProduct().getSellerId().equals(userId) && reservationDomain.isSellerCompleteRequest()) {
            throw new ExpectedException(ExceptionCode.RESERVATION_EXIST_CHECK);
        }
        if(reservationDomain.getBuyer().getId().equals(userId) && reservationDomain.isBuyerCompleteRequest()) {
            throw new ExpectedException(ExceptionCode.RESERVATION_EXIST_CHECK);
        }

        // 사용자 권한 확인
        boolean isBuyer = reservationDomain.getBuyer().getId().equals(userId);
        boolean isSeller = reservationDomain.getProduct().getSellerId().equals(userId);

        if(!isBuyer && !isSeller) {
            throw new ExpectedException(ExceptionCode.UNAUTHORIZED_ACTION);
        }

        // 요청 상태 업데이트
        ReservationDomain updatedReservationDomain = reservationDomain.completeRequest(isBuyer, isSeller);
        reservationPort.save(updatedReservationDomain,
                ProductMapper.domainToPersistence(updatedReservationDomain.getProduct()),
                UserMapper.domainToPersistence(updatedReservationDomain.getBuyer()));

        if(updatedReservationDomain.isBuyerCompleteRequest() && updatedReservationDomain.isSellerCompleteRequest()) {
            // 예약 : 예약 중 -> 거래 완료 변경
            updatedReservationDomain = updatedReservationDomain.updateState(Reservation.ReservationState.COMPLETED);
            // 상품 : 판매 중 -> 거래 완료 변경
            ProductDomain productDomain = reservationDomain.getProduct().updatedState(Product.SaleState.SOLD_OUT);
            productPort.save(productDomain);

            // 판매자에게 대금 지급
            UserDomain userDomain = userPort.read(reservationDomain.getProduct().getId());
            Money updatedCash = userDomain.getCash().add(updatedReservationDomain.getPrice());
            userDomain = userDomain.updatedCash(updatedCash);
            userPort.save(userDomain);

            reservationPort.save(updatedReservationDomain,
                    ProductMapper.domainToPersistence(updatedReservationDomain.getProduct()),
                    UserMapper.domainToPersistence(updatedReservationDomain.getBuyer()));

            return ReservationMapper.domainToDto(updatedReservationDomain);
        }

        return ReservationMapper.domainToDto(updatedReservationDomain);
    }
}
