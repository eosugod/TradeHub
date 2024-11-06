package com.eosuGod.TradeHub.product.mapper;

import com.eosuGod.TradeHub.product.dto.request.RequestCreateProductDto;
import com.eosuGod.TradeHub.product.dto.response.ResponseProductDto;
import com.eosuGod.TradeHub.product.entity.Product;
import com.eosuGod.TradeHub.product.vo.Address;
import com.eosuGod.TradeHub.product.vo.Money;

public class ProductMapper {
    static public Product toEntity(RequestCreateProductDto requestDto) {
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

    static public ResponseProductDto toResponseDto(Product product) {
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

