package com.eosugod.tradehub.user.entity;

import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
