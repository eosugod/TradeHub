package com.eosugod.tradehub.product.domain;

import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.vo.Address;
import com.eosugod.tradehub.product.vo.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDomain {
    private Long id;
    private Long sellerId; // 판매자 ID
    private Long buyerId; // 구매자 ID
    private Money price; // 가격
    private String title; // 제목
    private String text; // 내용
    private Address locationCode; // 지역 코드
    private Product.SaleState state; // 판매 상태 (판매중, 예약중, 판매완료)
    private String thumbNailImage; // 썸네일 이미지 URL
}
