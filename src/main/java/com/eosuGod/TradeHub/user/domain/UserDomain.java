package com.eosugod.tradehub.user.domain;

import com.eosugod.tradehub.user.vo.Address;
import com.eosugod.tradehub.user.vo.Money;
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
}
