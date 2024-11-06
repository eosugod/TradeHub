package com.eosuGod.TradeHub.product.dto.response;

import com.eosuGod.TradeHub.product.entity.Product.SaleState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class ResponseProductDto {
    private Long id;
    private Long sellerId;
    private Long buyerId;
    private BigDecimal price;
    private String title;
    private String text;
    private String locationCode;
    private SaleState state;
    private String thumbNailImage;

    public ResponseProductDto(Long id, Long sellerId, Long buyerId, BigDecimal price, String title, String text, String locationCode, SaleState state, String thumbNailImage) {
        this.id = id;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.price = price;
        this.title = title;
        this.text = text;
        this.locationCode = locationCode;
        this.state = state;
        this.thumbNailImage = thumbNailImage;
    }
}
