package com.eosugod.tradehub.user.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ResponseUserDto(Long id, String name, String nickName, String account, BigDecimal cash, String address) {
}
