package com.eosugod.tradehub.user.entity;

import com.eosugod.tradehub.user.vo.Address;
import com.eosugod.tradehub.user.vo.Money;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickName;
    private String account;
    @Embedded
    Money cash;
    @Embedded
    Address address;
}
