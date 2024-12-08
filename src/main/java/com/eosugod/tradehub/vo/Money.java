package com.eosugod.tradehub.vo;

import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
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

    public Money subtract(Money amount) {
        if(amount == null || this.cash.compareTo(amount.cash) < 0) {
            throw new ExpectedException(ExceptionCode.NOT_ENOUGH_POINT);
        }
        return new Money(this.cash.subtract(amount.cash));
    }
}
