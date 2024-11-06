package com.eosugod.tradehub.product.mapper;

import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.vo.Address;
import com.eosugod.tradehub.product.vo.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {
    public static Product toEntity(RequestCreateProductDto requestDto) {
        Product product = new Product();
        product.setSellerId(requestDto.getSellerId());
        product.setPrice(new Money(requestDto.getPrice()));
        product.setTitle(requestDto.getTitle());
        product.setText(requestDto.getText());
        product.setLocationCode(new Address(requestDto.getLocationCode()));
        product.setState(Product.SaleState.FOR_SALE); // 기본 상태 판매 중 설정
        product.setThumbNailImage(requestDto.getThumbNailImage());
        return product;
    }

    public static ResponseProductDto toResponseDto(Product product) {
        return new ResponseProductDto(
                product.getId(),
                product.getSellerId(),
                product.getBuyerId(),
                product.getPrice().getValue(),
                product.getTitle(),
                product.getText(),
                product.getLocationCode().getValue(),
                product.getState(),
                product.getThumbNailImage()
        );
    }
}

