package com.eosugod.tradehub.user.domain;

import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDomain {
    private final Long id;
    private final String name;
    private final String nickName;
    private final String account;
    private final Money cash;
    private final Address address;

    public UserDomain updatedCash(Money newCash) {
        return UserDomain.builder()
                .id(this.id)
                .name(this.name)
                .nickName(this.nickName)
                .account(this.account)
                .cash(newCash)
                .address(this.address)
                .build();
    }
}
