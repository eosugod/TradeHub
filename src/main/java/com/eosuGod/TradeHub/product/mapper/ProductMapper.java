package com.eosugod.tradehub.product.mapper;

import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.request.RequestUpdateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.vo.Address;
import com.eosugod.tradehub.product.vo.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {
    public static Product toEntity(RequestCreateProductDto requestDto) {
        return Product.builder()
                      .sellerId(requestDto.getSellerId())
                      .price(new Money(requestDto.getPrice()))
                      .title(requestDto.getTitle())
                      .text(requestDto.getText())
                      .locationCode(new Address(requestDto.getLocationCode()))
                      .state(Product.SaleState.FOR_SALE) // 기본 상태 판매 중 설정
                      .thumbNailImage(requestDto.getThumbNailImage())
                      .build();
    }

    public static Product toEntity(RequestUpdateProductDto requestDto, Product product) {
        return Product.builder()
                .id(product.getId())
                .sellerId(product.getSellerId())
                .price(new Money(requestDto.getPrice()))
                .title(requestDto.getTitle())
                .text(requestDto.getText())
                .locationCode(new Address(requestDto.getLocationCode()))
                .state(product.getState())
                .thumbNailImage(requestDto.getThumbNailImage())
                .build();
    }

    public static ResponseProductDto toResponseDto(Product product) {
        return ResponseProductDto.builder()
                .id(product.getId())
                .sellerId(product.getSellerId())
                .buyerId(product.getBuyerId())
                .price(product.getPrice().getValue())
                .title(product.getTitle())
                .text(product.getText())
                .locationCode(product.getLocationCode().getValue())
                .state(product.getState())
                .thumbNailImage(product.getThumbNailImage())
                .build();
    }
}

