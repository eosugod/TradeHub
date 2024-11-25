package com.eosugod.tradehub.product.domain;

import com.eosugod.tradehub.product.dto.request.RequestUpdateProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.vo.Address;
import com.eosugod.tradehub.product.vo.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDomain {
    private final Long id;
    private final Long sellerId; // 판매자 ID
    private final Long buyerId; // 구매자 ID
    private final Money price; // 가격
    private final String title; // 제목
    private final String text; // 내용
    private final Address locationCode; // 지역 코드
    private final Product.SaleState state; // 판매 상태 (판매중, 예약중, 판매완료)
    private final String thumbNailImage; // 썸네일 이미지 URL

    public ProductDomain update(RequestUpdateProductDto dto) {
        return ProductDomain.builder()
                .id(this.id)
                .sellerId(this.sellerId)
                .buyerId(this.buyerId)
                .price(new Money(dto.getPrice()))
                .title(dto.getTitle())
                .text(dto.getText())
                .locationCode(new Address(dto.getLocationCode()))
                .state(this.state)
                .thumbNailImage(dto.getThumbNailImage())
                .build();
    }
}
