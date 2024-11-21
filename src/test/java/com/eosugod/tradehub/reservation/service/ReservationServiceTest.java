package com.eosugod.tradehub.reservation.service;

import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.repository.ProductRepository;
import com.eosugod.tradehub.product.vo.Address;
import com.eosugod.tradehub.product.vo.Money;
import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
import com.eosugod.tradehub.reservation.dto.response.ResponseReservationDto;
import com.eosugod.tradehub.reservation.entity.Reservation;
import com.eosugod.tradehub.reservation.repository.ReservationRepository;
import com.eosugod.tradehub.user.entity.Users;
import com.eosugod.tradehub.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("예약 생성")
    void testCreateReservation() {
        // given
        Long productId = 1L;
        Long sellerId = 2L;
        Long buyerId = 3L;

        RequestCreateReservationDto requestDto = RequestCreateReservationDto.builder()
                .productId(productId)
                .buyerId(buyerId)
                .price(BigDecimal.valueOf(10000))
                .locationCode("1234567890")
                .confirmedAt(LocalDateTime.now())
                .build();

        Product product = Product.builder()
                .id(productId)
                .sellerId(sellerId)
                .build();

        Users user = Users.builder()
                .id(buyerId)
                .name("Test Buyer")
                .build();

        Reservation reservation = Reservation.builder()
                .product(product)
                .user(user)
                .price(new Money(requestDto.getPrice()))
                .locationCode(new Address(requestDto.getLocationCode()))
                .confirmedAt(LocalDateTime.now())
                .build();
        reservation.setProduct(product);
        reservation.setUser(user);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(userRepository.findById(buyerId)).thenReturn(Optional.of(user));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // when
        ResponseReservationDto responseDto = reservationService.createReservation(requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(productId, responseDto.getProductId());
        assertEquals(sellerId, responseDto.getSellerId());
        assertEquals(buyerId, responseDto.getBuyerId());
    }
}
