package com.eosugod.tradehub.user.service;

import com.eosugod.tradehub.user.validator.UserValidator;
import com.eosugod.tradehub.user.domain.UserDomain;
import com.eosugod.tradehub.user.dto.request.RequestCreateUserDto;
import com.eosugod.tradehub.user.dto.response.ResponseUserDto;
import com.eosugod.tradehub.user.mapper.UserMapper;
import com.eosugod.tradehub.user.port.UserPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserPort userPort;
    private final UserValidator userValidator;

    @Transactional
    public ResponseUserDto createUser(RequestCreateUserDto dto) {
        userValidator.existAccount(dto.account());
        userValidator.existNickName(dto.nickName());

        UserDomain userDomain = userPort.save(dto);
        return UserMapper.domainToDto(userDomain);
    }

}
