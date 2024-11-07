package com.eosugod.tradehub.user.adapter;

import com.eosugod.tradehub.user.domain.UserDomain;
import com.eosugod.tradehub.user.dto.request.RequestCreateUserDto;
import com.eosugod.tradehub.user.mapper.UserMapper;
import com.eosugod.tradehub.user.port.UserPort;
import com.eosugod.tradehub.user.repository.UserRepository;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserPort {
    private final UserRepository userRepository;

    @Override
    public UserDomain read(Long id) {
        return UserMapper.persistenceToDomain(userRepository.findById(id).orElseThrow(
                () -> new ExpectedException(ExceptionCode.NOT_EXIST_USER)
        ));
    }

    @Override
    public UserDomain save(RequestCreateUserDto userDto) {
        return UserMapper.persistenceToDomain(userRepository.save(UserMapper.dtoTopersistence(userDto)));
    }

    @Override
    public boolean existsByAccount(String account) {
        return userRepository.existsByAccount(account);
    }

    @Override
    public boolean existsByNickName(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

}
