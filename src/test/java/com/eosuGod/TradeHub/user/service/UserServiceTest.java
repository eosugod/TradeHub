package com.eosugod.tradehub.user.service;

import com.eosugod.tradehub.user.validator.UserValidator;
import com.eosugod.tradehub.user.domain.UserDomain;
import com.eosugod.tradehub.user.dto.request.RequestCreateUserDto;
import com.eosugod.tradehub.user.dto.response.ResponseUserDto;
import com.eosugod.tradehub.user.port.UserPort;
import com.eosugod.tradehub.user.vo.Address;
import com.eosugod.tradehub.user.vo.Money;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserPort userPort;
    @Mock
    private UserValidator userValidator;

    @DisplayName("회원을 등록할 수 있다.")
    @Test
    void createUser() {
        //given
        RequestCreateUserDto request = RequestCreateUserDto.builder()
                                                           .account("000-0000-000-00")
                                                           .name("어수현")
                                                           .nickName("죠즈")
                                                           .locationCode("1111100000")
                                                           .build();
        UserDomain expect = UserDomain.builder()
                                      .id(1L)
                                      .account("000-0000-000-00")
                                      .name("어수현")
                                      .nickName("죠즈")
                                      .address(new Address("1111100000"))
                                      .cash(new Money())
                                      .build();

        given(userPort.save(request)).willReturn(expect);

        //when
        ResponseUserDto user = userService.createUser(request);

        //then
        verify(userPort, times(1)).save(any());
    }

    @DisplayName("이미 등록된 계좌라면 회원 등록이 불가능하다.")
    @Test
    void existUser() {
        //given
        RequestCreateUserDto request = RequestCreateUserDto.builder()
                                                           .account("000-0000-000-00")
                                                           .name("어수현")
                                                           .nickName("죠즈")
                                                           .locationCode("1111100000")
                                                           .build();
        // When
        doThrow(new ExpectedException(ExceptionCode.EXIST_ACCOUNT)).when(userValidator).existAccount(request.account());

        // then
        ExpectedException exception = assertThrows(ExpectedException.class, () -> {
            userService.createUser(request);
        });

        // 확인
        assertEquals(exception.getCode(), 1001);
    }

    @DisplayName("이미 등록된 닉네임이라면 회원 등록이 불가능하다.")
    @Test
    void existNickName() {
        //given
        RequestCreateUserDto request = RequestCreateUserDto.builder()
                                                           .account("000-0000-000-00")
                                                           .name("어수현")
                                                           .nickName("죠즈")
                                                           .locationCode("1111100000")
                                                           .build();
        // When
        doThrow(new ExpectedException(ExceptionCode.EXIST_NICKNAME)).when(userValidator).existNickName(request.nickName());
        // then
        ExpectedException exception = assertThrows(ExpectedException.class, () -> {
            userService.createUser(request);
        });

        // 확인
        assertEquals(exception.getCode(), 1002);
    }


}