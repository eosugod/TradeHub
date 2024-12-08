package com.eosugod.tradehub.product.mapper;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {
    public static ProductDomain persistenceToDomain(Product product) {
        return ProductDomain.builder()
                .id(product.getId())
                .sellerId(product.getSellerId())
                .price(new Money(product.getPrice().getValue()))
                .title(product.getTitle())
                .text(product.getText())
                .locationCode(new Address(product.getLocationCode().getValue()))
                .state(product.getState())
                .thumbNailImage(product.getThumbNailImage())
                .build();
    }

    public static Product dtoToPersistence(RequestCreateProductDto dto) {
        return Product.builder()
                      .sellerId(dto.getSellerId())
                      .price(new Money(dto.getPrice()))
                      .title(dto.getTitle())
                      .text(dto.getText())
                      .locationCode(new Address(dto.getLocationCode()))
                      .state(Product.SaleState.FOR_SALE)
                      .thumbNailImage(dto.getThumbNailImage())
                      .build();
    }

    public static Product domainToPersistence(ProductDomain productDomain) {
        return Product.builder()
                      .id(productDomain.getId())
                      .sellerId(productDomain.getSellerId())
                      .price(productDomain.getPrice())
                      .title(productDomain.getTitle())
                      .text(productDomain.getText())
                      .locationCode(productDomain.getLocationCode())
                      .state(productDomain.getState())
                      .thumbNailImage(productDomain.getThumbNailImage())
                      .build();
    }

    public static ResponseProductDto domainToDto(ProductDomain productDomain) {
        return ResponseProductDto.builder()
                                 .id(productDomain.getId())
                                 .sellerId(productDomain.getSellerId())
                                 .price(productDomain.getPrice().getValue())
                                 .title(productDomain.getTitle())
                                 .text(productDomain.getText())
                                 .locationCode(productDomain.getLocationCode().getValue())
                                 .state(productDomain.getState())
                                 .thumbNailImage(productDomain.getThumbNailImage())
                                 .build();
    }
}

