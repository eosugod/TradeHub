package com.eosugod.tradehub.reservation.domain;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.vo.Address;
import com.eosugod.tradehub.product.vo.Money;
import com.eosugod.tradehub.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationDomain {
    private final Long id;
    private final UserDomain buyer;
    private final ProductDomain product;
    private final Money price;
    private final Address locationCode;
    private final LocalDateTime confirmedAt;
}
