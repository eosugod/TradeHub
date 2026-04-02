package com.eosugod.tradehub.user.mapper;

import com.eosugod.tradehub.user.domain.UserDomain;
import com.eosugod.tradehub.user.dto.request.RequestCreateUserDto;
import com.eosugod.tradehub.user.dto.response.ResponseUserDto;
import com.eosugod.tradehub.user.entity.Users;
import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static Users domainToPersistence(UserDomain userDomain) {
        return Users.builder()
                    .id(userDomain.getId())
                    .name(userDomain.getName())
                    .account(userDomain.getAccount())
                    .nickName(userDomain.getNickName())
                    .cash(userDomain.getCash())
                    .address(userDomain.getAddress())
                    .version(userDomain.getVersion())
                    .build();
    }

    public static UserDomain persistenceToDomain(Users usersEntity) {
        return UserDomain.builder()
                         .id(usersEntity.getId())
                         .name(usersEntity.getName())
                         .account(usersEntity.getAccount())
                         .address(usersEntity.getAddress())
                         .cash(usersEntity.getCash())
                         .nickName(usersEntity.getNickName())
                         .version(usersEntity.getVersion())
                         .build();
    }

    public static UserDomain dtoToDomain(RequestCreateUserDto dto) {
        return UserDomain.builder()
                         .account(dto.account())
                         .cash(new Money())
                         .name(dto.name())
                         .nickName(dto.nickName())
                         .address(new Address(dto.locationCode()))
                         .build();
    }

    public static Users dtoToPersistence(RequestCreateUserDto dto) {
        return Users.builder()
                    .name(dto.name())
                    .account(dto.account())
                    .nickName(dto.nickName())
                    .cash(new Money())
                    .address(new Address(dto.locationCode()))
                    .build();
    }

    public static ResponseUserDto domainToDto(UserDomain userDomain) {
        return ResponseUserDto.builder()
                              .id(userDomain.getId())
                              .name(userDomain.getName())
                              .account(userDomain.getAccount())
                              .nickName(userDomain.getNickName())
                              .cash(userDomain.getCash().getValue())
                              .address(userDomain.getAddress().getValue())
                              .build();
    }
}
