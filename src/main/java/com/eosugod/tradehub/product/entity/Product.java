package com.eosugod.tradehub.product.entity;

import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sellerId; // 판매자 ID
    private Long buyerId; // 구매자 ID
    @Embedded
    private Money price; // 가격
    private String title; // 제목
    private String text; // 내용
    @Embedded
    private Address locationCode; // 지역 코드
    @Enumerated(EnumType.STRING)
    private SaleState state; // 판매 상태 (판매중, 예약중, 판매완료)
    private String thumbNailImage; // 썸네일 이미지 URL

    public enum SaleState {
        FOR_SALE, // 판매 중
        RESERVED, // 예약 중
        SOLD_OUT // 판매 완료
    }
}
