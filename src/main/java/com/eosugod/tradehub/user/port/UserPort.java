package com.eosugod.tradehub.user.port;


import com.eosugod.tradehub.user.domain.UserDomain;
import com.eosugod.tradehub.user.dto.request.RequestCreateUserDto;

public interface UserPort {
    UserDomain read(Long id);
    UserDomain save(RequestCreateUserDto dto);

    boolean existsByAccount(String account);

    boolean existsByNickName(String nickName);
}
