package com.eosugod.tradehub.user.service;

import com.eosugod.tradehub.user.domain.UserDomain;
import com.eosugod.tradehub.user.dto.request.RequestCreateUserDto;
import com.eosugod.tradehub.user.dto.response.ResponseUserDto;
import com.eosugod.tradehub.user.entity.Users;
import com.eosugod.tradehub.user.mapper.UserMapper;
import com.eosugod.tradehub.user.port.UserPort;
import com.eosugod.tradehub.user.validator.UserValidator;
import com.eosugod.tradehub.vo.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserPort userPort;
    private final UserValidator userValidator;

    @Transactional
    public ResponseUserDto createUser(RequestCreateUserDto dto) {
        userValidator.existAccount(dto.account());
        userValidator.existNickName(dto.nickName());

        Users user = UserMapper.dtoToPersistence(dto);
        UserDomain userDomain = UserMapper.persistenceToDomain(user);
        return UserMapper.domainToDto(userPort.save(userDomain));
    }

    @Transactional(readOnly = true)
    public ResponseUserDto readUser(Long id) {
        UserDomain userDomain = userPort.read(id);
        return UserMapper.domainToDto(userDomain);
    }

    @Transactional
    public ResponseUserDto chargeUser(Long id, Long amount) {
        userValidator.validateChargeAmount(amount);

        UserDomain userDomain = userPort.read(id);
        Money newCash = userDomain.getCash().add(new Money(BigDecimal.valueOf(amount)));
        UserDomain updatedUserDomain = userDomain.updatedCash(newCash);

        return UserMapper.domainToDto(userPort.save(updatedUserDomain));
    }

}
