package com.eosugod.tradehub.user.domain;

import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDomain {
    private Long id;
    private String name;
    private String nickName;
    private String account;
    Money cash;
    Address address;

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
