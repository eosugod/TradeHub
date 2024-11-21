package com.eosugod.tradehub.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    EXIST_ACCOUNT(1001, "이미 등록된 계좌입니다."),
    EXIST_NICKNAME(1002, "이미 등록된 닉네임입니다."),
    NOT_EXIST_USER(4001, "존재하지 않는 유저입니다."),
    PRODUCT_NOT_FOUND(2001, "찾을 수 없는 상품입니다."),
    EXIST_RESERVATION(5001, "이미 예약 신청한 상품입니다.");

    private final int code;
    private final String message;
}
