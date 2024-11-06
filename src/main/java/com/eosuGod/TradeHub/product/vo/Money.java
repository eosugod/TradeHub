package com.eosuGod.TradeHub.product.vo;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
public class Money {
    private BigDecimal price;

    public Money(BigDecimal price) {
        if (price == null || price.signum() <= 0) {
            throw new IllegalArgumentException("금액은 null이거나 음수일 수 없습니다.");
        }
        this.price = price;
    }

    public BigDecimal getValue() {
        return price;
    }
}
