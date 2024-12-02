//package com.eosugod.tradehub.reservation.service;
//
//import com.eosugod.tradehub.product.entity.Product;
//import com.eosugod.tradehub.product.repository.ProductRepository;
//import com.eosugod.tradehub.product.vo.Address;
//import com.eosugod.tradehub.product.vo.Money;
//import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
//import com.eosugod.tradehub.reservation.dto.response.ResponseReservationDto;
//import com.eosugod.tradehub.reservation.entity.Reservation;
//import com.eosugod.tradehub.reservation.repository.ReservationRepository;
//import com.eosugod.tradehub.user.entity.Users;
//import com.eosugod.tradehub.user.repository.UserRepository;
//import com.eosugod.tradehub.util.ExpectedException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ReservationServiceTest {
//    @InjectMocks
//    private ReservationService reservationService;
//    @Mock
//    ReservationRepository reservationRepository;
//    @Mock
//    ProductRepository productRepository;
//    @Mock
//    UserRepository userRepository;
//
//    @Test
//    @DisplayName("예약 생성 성공")
//    void testCreateReservation() {
//        // given
//        Long productId = 1L;
//        Long sellerId = 2L;
//        Long buyerId = 3L;
//
//        RequestCreateReservationDto requestDto = RequestCreateReservationDto.builder()
//                .productId(productId)
//                .buyerId(buyerId)
//                .price(BigDecimal.valueOf(10000))
//                .locationCode("1234567890")
//                .confirmedAt(LocalDateTime.now())
//                .build();
//
//        Product product = Product.builder()
//                .id(productId)
//                .sellerId(sellerId)
//                .state(Product.SaleState.FOR_SALE)
//                .build();
//
//        Users user = Users.builder()
//                .id(buyerId)
//                .name("Test Buyer")
//                .cash(new com.eosugod.tradehub.user.vo.Money(BigDecimal.valueOf(10000)))
//                .build();
//
//        Reservation reservation = Reservation.builder()
//                .product(product)
//                .user(user)
//                .price(new Money(requestDto.getPrice()))
//                .locationCode(new Address(requestDto.getLocationCode()))
//                .confirmedAt(LocalDateTime.now())
//                .build();
//        reservation.setProduct(product);
//
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//        when(userRepository.findById(buyerId)).thenReturn(Optional.of(user));
//        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
//
//        // when
//        ResponseReservationDto responseDto = reservationService.createReservation(requestDto);
//
//        // then
//        assertNotNull(responseDto);
//        assertEquals(productId, responseDto.getProductId());
//        assertEquals(sellerId, responseDto.getSellerId());
//        assertEquals(buyerId, responseDto.getBuyerId());
//    }
//
//    @Test
//    @DisplayName("예약 생성 실패1 - 보유 잔액 부족")
//    void testCreateReservationFail1() {
//        // given
//        Long productId = 1L;
//        Long sellerId = 2L;
//        Long buyerId = 3L;
//
//        RequestCreateReservationDto requestDto = RequestCreateReservationDto.builder()
//                                                                            .productId(productId)
//                                                                            .buyerId(buyerId)
//                                                                            .price(BigDecimal.valueOf(10000))
//                                                                            .locationCode("1234567890")
//                                                                            .confirmedAt(LocalDateTime.now())
//                                                                            .build();
//
//        Product product = Product.builder()
//                                 .id(productId)
//                                 .sellerId(sellerId)
//                                 .state(Product.SaleState.FOR_SALE)
//                                 .build();
//
//        Users user = Users.builder()
//                          .id(buyerId)
//                          .name("Test Buyer")
//                          .cash(new com.eosugod.tradehub.user.vo.Money(BigDecimal.valueOf(5000))) // 보유 잔액
//                          .build();
//
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//        when(userRepository.findById(buyerId)).thenReturn(Optional.of(user));
//
//        // when then
//        assertThrows(ExpectedException.class, () -> reservationService.createReservation(requestDto));
//    }
//
//    @Test
//    @DisplayName("예약 생성 실패2 - 판매중인 상품이 아님")
//    void testCreateReservationFail2() {
//        // given
//        Long productId = 1L;
//        Long sellerId = 2L;
//        Long buyerId = 3L;
//
//        RequestCreateReservationDto requestDto = RequestCreateReservationDto.builder()
//                                                                            .productId(productId)
//                                                                            .buyerId(buyerId)
//                                                                            .price(BigDecimal.valueOf(10000))
//                                                                            .locationCode("1234567890")
//                                                                            .confirmedAt(LocalDateTime.now())
//                                                                            .build();
//
//        Product product = Product.builder()
//                                 .id(productId)
//                                 .sellerId(sellerId)
//                                 .state(Product.SaleState.RESERVED) // 이미 예약된 상품
//                                 .build();
//
//        Users user = Users.builder()
//                          .id(buyerId)
//                          .name("Test Buyer")
//                          .cash(new com.eosugod.tradehub.user.vo.Money(BigDecimal.valueOf(10000)))
//                          .build();
//
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//        when(userRepository.findById(buyerId)).thenReturn(Optional.of(user));
//
//        // when then
//        assertThrows(ExpectedException.class, () -> reservationService.createReservation(requestDto));
//    }
//
//    @Test
//    @DisplayName("예약 생성 실패3 - 존재하지 않는 상품")
//    void testCreateReservationFail3() {
//        // given
//        Long productId = 1L;
//        Long buyerId = 3L;
//
//        RequestCreateReservationDto requestDto = RequestCreateReservationDto.builder()
//                                                                            .productId(productId)  // 존재하지 않는 상품 ID
//                                                                            .buyerId(buyerId)
//                                                                            .price(BigDecimal.valueOf(10000))
//                                                                            .locationCode("1234567890")
//                                                                            .confirmedAt(LocalDateTime.now())
//                                                                            .build();
//
//        when(productRepository.findById(productId)).thenReturn(Optional.empty());
//
//        // when then
//        assertThrows(ExpectedException.class, () -> reservationService.createReservation(requestDto));
//    }
//
//    @Test
//    @DisplayName("해당 상품에 대한 전체 예약 조회")
//    void testGetReservationsByProduct() {
//        // given
//        Long productId = 1L;
//        List<Reservation> reservations = IntStream.range(0, 10)
//                                                  .mapToObj(i -> Reservation.builder()
//                                                                            .id((long) i)
//                                                                            .product(Product.builder().id(productId).build())
//                                                                            .user(Users.builder().id((long) i).build())
//                                                                            .price(new Money(BigDecimal.valueOf(10000)))
//                                                                            .locationCode(new Address("1234567890"))
//                                                                            .confirmedAt(LocalDateTime.now().minusDays(i))
//                                                                            .build())
//                                                  .collect(Collectors.toList());
//
//        Page<Reservation> reservationPage = new PageImpl<>(reservations);
//
//        when(reservationRepository.findAllByProductId(eq(productId), any(Pageable.class))).thenReturn(reservationPage);
//
//        // when
//        Page<ResponseReservationDto> response = reservationService.getAllReservations(productId, 0, 20);
//
//        // then
//        assertNotNull(response);
//        assertEquals(10, response.getContent().size());
//    }
//}
