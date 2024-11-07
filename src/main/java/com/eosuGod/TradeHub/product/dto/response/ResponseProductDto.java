package com.eosugod.tradehub.product.dto.response;

import com.eosugod.tradehub.product.entity.Product.SaleState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
