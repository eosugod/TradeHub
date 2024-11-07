package com.eosugod.tradehub.user.vo;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Money {
    BigDecimal cash;

    public Money(BigDecimal cash) {
        this.cash = cash;
    }

    public Money() {
        this.cash = BigDecimal.ZERO;
    }

    public BigDecimal getValue() {
        return cash;
    }

}
