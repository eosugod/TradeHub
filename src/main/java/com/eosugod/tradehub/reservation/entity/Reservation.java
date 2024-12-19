package com.eosugod.tradehub.reservation.entity;

import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.user.entity.Users;
import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Users buyer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Embedded
    private Money price;
    @Embedded
    private Address locationCode;
    private LocalDateTime confirmedAt;
    @Enumerated(EnumType.STRING)
    private ReservationState state;
    private boolean buyerCompleteRequest = false;
    private boolean sellerCompleteRequest = false;

    public enum ReservationState {
        PENDING, // 예약 대기
        CONFIRMED, // 예약 확정
        CANCELLED, // 예약 취소
        COMPLETED // 거래 완료
    }
}
