package com.eosugod.tradehub.reservation.service;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.entity.Product;
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
import com.eosugod.tradehub.user.port.UserPort;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private ReservationPort reservationPort;
    @Mock
    private ProductPort productPort;
    @Mock
    private UserPort userPort;
    public ReservationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("예약 생성")
    void testCreateReservation_Success() {
        // Given
        Long buyerId = 1L;
        Long productId = 2L;
        BigDecimal price = BigDecimal.valueOf(1000);
        String locationCode = "1234567890";

        RequestCreateReservationDto requestDto = RequestCreateReservationDto.builder()
                                                                            .buyerId(buyerId)
                                                                            .productId(productId)
                                                                            .price(price)
                                                                            .locationCode(locationCode)
                                                                            .confirmedAt(LocalDateTime.now())
                                                                            .build();

        UserDomain userDomain = UserDomain.builder()
                                          .id(buyerId)
                                          .cash(new Money(BigDecimal.valueOf(2000)))
                                          .build();

        ProductDomain productDomain = ProductDomain.builder()
                                                   .id(productId)
                                                   .price(new Money(price))
                                                   .state(Product.SaleState.FOR_SALE)
                                                   .locationCode(new Address(locationCode))
                                                   .build();

        ReservationDomain reservationDomain = ReservationDomain.builder()
                                                               .id(1L)
                                                               .buyer(userDomain)
                                                               .product(productDomain)
                                                               .price(new Money(price))
                                                               .locationCode(new Address(locationCode))
                                                               .confirmedAt(LocalDateTime.now())
                                                               .build();

        when(userPort.read(buyerId)).thenReturn(userDomain);
        when(productPort.findById(productId)).thenReturn(Optional.of(productDomain));
        when(reservationPort.existsByProductIdAndBuyerId(productId, buyerId)).thenReturn(false);
        when(reservationPort.save(any(), any(), any())).thenReturn(reservationDomain);

        // When
        ResponseReservationDto responseDto = reservationService.createReservation(requestDto);

        // Then
        assertNotNull(responseDto);
        assertEquals(buyerId, responseDto.getBuyerId());
        assertEquals(productId, responseDto.getProductId());
        verify(reservationPort, times(1)).save(any(), any(), any());
    }

    @Test
    @DisplayName("예약 생성 실패 - 잔액 부족")
    void testCreateReservation_Fail_NotEnoughPoint() {
        // Given
        Long buyerId = 1L;
        Long productId = 2L;
        BigDecimal price = BigDecimal.valueOf(1000);
        String locationCode = "1234567890";

        RequestCreateReservationDto requestDto = RequestCreateReservationDto.builder()
                                                                            .buyerId(buyerId)
                                                                            .productId(productId)
                                                                            .price(price)
                                                                            .locationCode(locationCode)
                                                                            .confirmedAt(LocalDateTime.now())
                                                                            .build();

        UserDomain userDomain = UserDomain.builder()
                                          .id(buyerId)
                                          .cash(new Money(BigDecimal.valueOf(500))) // 잔액 500
                                          .build();

        ProductDomain productDomain = ProductDomain.builder()
                                                   .id(productId)
                                                   .price(new Money(price))
                                                   .state(Product.SaleState.FOR_SALE)
                                                   .build();

        when(userPort.read(buyerId)).thenReturn(userDomain);
        when(productPort.findById(productId)).thenReturn(Optional.of(productDomain));

        // When & Then
        ExpectedException thrown = assertThrows(ExpectedException.class, () -> {
            reservationService.createReservation(requestDto);
        });
        assertEquals(ExceptionCode.NOT_ENOUGH_POINT.getCode(), thrown.getCode());
    }

    @Test
    @DisplayName("해당 상품의 모든 예약 목록 조회")
    void testGetAllReservations_Success() {
        // Given
        Long productId = 1L;
        int page = 0;
        int size = 2;

        UserDomain buyer = UserDomain.builder().id(1L).cash(new Money(BigDecimal.valueOf(2000))).build();
        ProductDomain product = ProductDomain.builder().id(productId).state(Product.SaleState.FOR_SALE).build();

        ReservationDomain reservation1 = ReservationDomain.builder()
                                                          .id(1L)
                                                          .buyer(buyer)
                                                          .product(product)
                                                          .price(new Money(BigDecimal.valueOf(1000)))
                                                          .locationCode(new Address("1234567890"))
                                                          .confirmedAt(LocalDateTime.now())
                                                          .build();

        ReservationDomain reservation2 = ReservationDomain.builder()
                                                          .id(2L)
                                                          .buyer(buyer)
                                                          .product(product)
                                                          .price(new Money(BigDecimal.valueOf(1500)))
                                                          .locationCode(new Address("123457890"))
                                                          .confirmedAt(LocalDateTime.now())
                                                          .build();

        List<ReservationDomain> reservations = Arrays.asList(reservation1, reservation2);
        Page<ReservationDomain> reservationPage = new PageImpl<>(reservations, PageRequest.of(page, size), reservations.size());

        when(reservationPort.findByProductId(productId, PageRequest.of(page, size))).thenReturn(reservationPage);

        // When
        Page<ResponseReservationDto> responsePage = reservationService.getAllReservations(productId, page, size);

        // Then
        assertNotNull(responsePage);
        assertEquals(2, responsePage.getContent().size());
        assertEquals(1L, responsePage.getContent().get(0).getId());
        assertEquals(2L, responsePage.getContent().get(1).getId());
        verify(reservationPort, times(1)).findByProductId(productId, PageRequest.of(page, size));
    }

    @Test
    @DisplayName("예약 확정")
    void testConfirmReservation_Success() {
        // Given
        Long reservationId = 1L;
        Long buyerId = 1L;
        Long productId = 2L;

        UserDomain buyer = UserDomain.builder()
                                     .id(buyerId)
                                     .cash(new Money(BigDecimal.valueOf(2000)))
                                     .build();

        ProductDomain product = ProductDomain.builder()
                                             .id(productId)
                                             .state(Product.SaleState.FOR_SALE)
                                             .price(new Money(BigDecimal.valueOf(1000)))
                                             .build();

        ReservationDomain reservationDomain = ReservationDomain.builder()
                                                               .id(reservationId)
                                                               .buyer(buyer)
                                                               .product(product)
                                                               .price(new Money(BigDecimal.valueOf(1000)))
                                                               .locationCode(new Address("1234567890"))
                                                               .confirmedAt(null)
                                                               .build();

        when(reservationPort.findById(reservationId)).thenReturn(Optional.of(reservationDomain));
        when(reservationPort.save(any(), any(), any())).thenReturn(reservationDomain);
        when(userPort.read(buyerId)).thenReturn(buyer);
        when(productPort.findById(productId)).thenReturn(Optional.of(product));

        // When
        ResponseReservationDto responseDto = reservationService.confirmReservation(reservationId);

        // Then
        assertNotNull(responseDto);
        assertEquals(reservationId, responseDto.getId());
        assertNotNull(responseDto.getConfirmedAt());
        assertEquals(buyerId, responseDto.getBuyerId());
        assertEquals(productId, responseDto.getProductId());
        assertEquals("12345678", responseDto.getLocationCode());

        // 상품 상태 검증
        ArgumentCaptor<ProductDomain> productCaptor = ArgumentCaptor.forClass(ProductDomain.class);
        verify(productPort, times(1)).save(productCaptor.capture());
        ProductDomain updatedProduct = productCaptor.getValue();
        assertEquals(Product.SaleState.RESERVED, updatedProduct.getState());
    }

    @Test
    @DisplayName("해당 유저의 확정된 단일 예약 조회 - 성공")
    void testGetConfirmedReservation_Success() {
        // Given
        Long reservationId = 1L;
        Long userId = 1L;
        ReservationDomain reservation = ReservationDomain.builder()
                                                         .id(reservationId)
                                                         .buyer(UserDomain.builder().id(userId).build())
                                                         .product(ProductDomain.builder().sellerId(2L).build())
                                                         .price(new Money(BigDecimal.valueOf(1000)))
                                                         .locationCode(new Address("1234567890"))
                                                         .state(Reservation.ReservationState.CONFIRMED)
                                                         .build();

        when(reservationPort.findById(reservationId)).thenReturn(Optional.of(reservation));

        // When
        ResponseReservationDto response = reservationService.getConfirmedReservation(reservationId, userId);

        // Then
        assertNotNull(response);
        assertEquals(reservationId, response.getId());
        verify(reservationPort).findById(reservationId);
    }

    @Test
    @DisplayName("해당 유저의 확정된 모든 예약 조회 - 성공")
    void testGetAllConfirmedReservations_Success() {
        // Given
        Long userId = 1L;
        int page = 0;
        int size = 20;

        Pageable pageable = PageRequest.of(page, size);

        List<ReservationDomain> buyerReservations = List.of(
                ReservationDomain.builder()
                                 .id(1L)
                                 .buyer(UserDomain.builder().id(userId).build())
                                 .product(ProductDomain.builder().sellerId(2L).build())
                                 .price(new Money(BigDecimal.valueOf(1000)))
                                 .locationCode(new Address("1234567890"))
                                 .state(Reservation.ReservationState.CONFIRMED)
                                 .build()
        );

        List<ReservationDomain> sellerReservations = List.of(
                ReservationDomain.builder()
                                 .id(2L)
                                 .buyer(UserDomain.builder().id(3L).build())
                                 .product(ProductDomain.builder().sellerId(userId).build())
                                 .price(new Money(BigDecimal.valueOf(2000)))
                                 .locationCode(new Address("9876543210"))
                                 .state(Reservation.ReservationState.CONFIRMED)
                                 .build()
        );

        when(reservationPort.findAllByBuyerIdAndState(userId, Reservation.ReservationState.CONFIRMED, pageable))
                .thenReturn(new PageImpl<>(buyerReservations, pageable, buyerReservations.size()));

        when(reservationPort.findAllBySellerIdAndState(userId, Reservation.ReservationState.CONFIRMED, pageable))
                .thenReturn(new PageImpl<>(sellerReservations, pageable, sellerReservations.size()));

        // When
        Page<ResponseReservationDto> responsePage = reservationService.getAllConfirmedReservations(userId, page, size);

        // Then
        assertNotNull(responsePage);
        assertEquals(2, responsePage.getTotalElements());
        assertEquals(2, responsePage.getContent().size());
        assertEquals(Reservation.ReservationState.CONFIRMED, responsePage.getContent().get(1).getState());

        verify(reservationPort).findAllByBuyerIdAndState(userId, Reservation.ReservationState.CONFIRMED, pageable);
        verify(reservationPort).findAllBySellerIdAndState(userId, Reservation.ReservationState.CONFIRMED, pageable);
    }
}
