package com.eosugod.tradehub.integrationTest;

import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.service.ProductService;
import com.eosugod.tradehub.reservation.domain.ReservationDomain;
import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
import com.eosugod.tradehub.reservation.port.ReservationPort;
import com.eosugod.tradehub.reservation.service.ReservationService;
import com.eosugod.tradehub.user.domain.UserDomain;
import com.eosugod.tradehub.user.dto.request.RequestCreateUserDto;
import com.eosugod.tradehub.user.port.UserPort;
import com.eosugod.tradehub.user.service.UserService;
import com.eosugod.tradehub.vo.Money;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Transactional
class IntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserPort userPort;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationPort reservationPort;

    @Test
    @DisplayName("낙관적 락 테스트")
    void optimisticLockingWithMultipleThreads() throws InterruptedException {
        // given
        Long sellerId = 1L;
        Long buyerId = 2L;
        RequestCreateUserDto userRequest1 = RequestCreateUserDto.builder()
                                                           .account("000-0000-000-00")
                                                           .name("어수현")
                                                           .nickName("죠즈")
                                                           .locationCode("1111100000")
                                                           .build();

        userService.createUser(userRequest1);
        RequestCreateUserDto userRequest2 = RequestCreateUserDto.builder()
                                                            .account("000-0000-000-01")
                                                            .name("이재혁")
                                                            .nickName("네츄럴나인")
                                                            .locationCode("1111100000")
                                                            .build();
        userService.createUser(userRequest2);
        userService.chargeUser(buyerId, 5000L);

        RequestCreateProductDto productRequest1 = RequestCreateProductDto.builder()
                .sellerId(sellerId)
                .locationCode("1111100000")
                .price(BigDecimal.valueOf(3000))
                .title("test")
                .text("test")
                .thumbNailImage("test_url")
                .build();
        productService.createProduct(productRequest1);
        RequestCreateProductDto productRequest2 = RequestCreateProductDto.builder()
                                                                         .sellerId(sellerId)
                                                                         .locationCode("1111100000")
                                                                         .price(BigDecimal.valueOf(4000))
                                                                         .title("test2")
                                                                         .text("test2")
                                                                         .thumbNailImage("test_url2")
                                                                         .build();
        productService.createProduct(productRequest2);

        RequestCreateReservationDto reservationRequest1 = RequestCreateReservationDto.builder()
                .productId(1L)
                .buyerId(buyerId)
                .price(BigDecimal.valueOf(3000))
                .locationCode("1111100000")
                .confirmedAt(LocalDateTime.now().plusHours(1))
                .build();
        reservationService.createReservation(reservationRequest1);
        RequestCreateReservationDto reservationRequest2 = RequestCreateReservationDto.builder()
                .productId(2L)
                .buyerId(buyerId)
                .price(BigDecimal.valueOf(4000))
                .locationCode("1111100000")
                .confirmedAt(LocalDateTime.now().plusHours(1))
                .build();
        reservationService.createReservation(reservationRequest2);


        UserDomain userDomain1 = userPort.read(2L);
        System.out.println("cash " + userDomain1.getCash().getValue());
        // when: 다중 스레드 환경에서 동일한 엔티티 업데이트 시도
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            Money updatedCash = userDomain1.getCash().subtract(new Money(BigDecimal.valueOf(2000)));
            UserDomain newUser = userDomain1.updatedCash(updatedCash);
            System.out.println(newUser.getCash().getValue());
            userPort.save(newUser);
        };
        Runnable task2 = () -> {
            Money updatedCash2 = userDomain1.getCash().subtract(new Money(BigDecimal.valueOf(4000)));
            UserDomain newUser2 = userDomain1.updatedCash(updatedCash2);
            System.out.println(newUser2.getCash().getValue());
            userPort.save(newUser2);
        };

//        Runnable task2 = () -> {
//            try {
//                reservationService.confirmReservation(2L, sellerId);
//                System.out.println("Task 2 완료");
//            } catch (OptimisticLockException e) {
//                System.out.println("Thread 2 : Exception 발생");
//            } finally {
//                latch.countDown();
//            }
//        };

        executorService.submit(task1);
        executorService.submit(task2);

        // Then: 최종 포인트 값 확인
        System.out.println("최종 포인트 값: " + userPort.read(2L).getCash().getValue());

        executorService.shutdown();
    }

//    @Test
//    @DisplayName("낙관적 락 테스트 - 단일 스레드")
//    void optimisticLockingWithSingleThread() throws InterruptedException {
//        // given
//        Long userId = 1L;
//
//        RequestCreateUserDto request = RequestCreateUserDto.builder()
//                                                       .account("000-0000-000-00")
//                                                       .name("어수현")
//                                                       .nickName("죠즈")
//                                                       .locationCode("1111100000")
//                                                       .build();
//
//        userService.createUser(request);
//        userService.chargeUser(userId, 5000L);
//
//        // when
//        UserDomain userDomain1 = userPort.read(userId);
//        Money updatedCash = userDomain1.getCash().subtract(new Money(BigDecimal.valueOf(3000)));
//        UserDomain updatedUserDomain1 = userDomain1.updatedCash(updatedCash);
//        userPort.save(updatedUserDomain1);
//
//        // then
//        UserDomain updatedUserDomain = userPort.read(userId);
//        System.out.println("최종 포인트 값: " + updatedUserDomain.getCash().getValue());
//    }
}
