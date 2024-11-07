package com.eosugod.tradehub.user.service;

import com.eosugod.tradehub.user.domain.UserDomain;
import com.eosugod.tradehub.user.dto.request.RequestCreateUserDto;
import com.eosugod.tradehub.user.dto.response.ResponseUserDto;
import com.eosugod.tradehub.user.port.UserPort;
import com.eosugod.tradehub.user.validator.UserValidator;
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

import static org.junit.jupiter.api.Assertions.*;
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
        userService.createUser(request);

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
        assertEquals(1001, exception.getCode());
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
        doThrow(new ExpectedException(ExceptionCode.EXIST_NICKNAME)).when(userValidator)
                                                                    .existNickName(request.nickName());
        // then
        ExpectedException exception = assertThrows(ExpectedException.class, () -> {
            userService.createUser(request);
        });

        // 확인
        assertEquals(1002, exception.getCode());
    }

    @DisplayName("유저가 존재하지 않으면 Exception이 발생한다.")
    @Test
    void notExistUser() {
        //given
        Long userId = 1L;

        // When
        doThrow(new ExpectedException(ExceptionCode.NOT_EXIST_USER)).when(userPort).read(userId);

        // then
        ExpectedException exception = assertThrows(ExpectedException.class, () -> {
            userService.readUser(1L);
        });

        // 확인
        assertEquals(4001, exception.getCode());
    }

    @DisplayName("id를 기반으로 유저를 조회할 수 있다.")
    @Test
    void readUser() {
        //given
        Long userId = 1L;
        UserDomain expect = UserDomain.builder()
                                      .id(1L)
                                      .account("000-0000-000-00")
                                      .name("어수현")
                                      .nickName("죠즈")
                                      .address(new Address("1111100000"))
                                      .cash(new Money())
                                      .build();

        //given
        given(userPort.read(userId)).willReturn(expect);

        //when
        ResponseUserDto result = userService.readUser(userId);

        //then
        assertNotNull(result);
        assertEquals(expect.getId(), result.id());
        assertEquals(expect.getAccount(), result.account());
        assertEquals(expect.getName(), result.name());
        assertEquals(expect.getNickName(), result.nickName());
        assertEquals(expect.getAddress().getValue(), result.address());
        assertEquals(expect.getCash().getCash(), result.cash());

        // 확인
        verify(userPort, times(1)).read(any());
    }


}