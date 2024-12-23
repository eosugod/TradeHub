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
    PRODUCT_NOT_FOR_SALE(2002, "판매중인 상품이 아닙니다."),
    PRODUCT_HAS_RESERVATION(2003, "신청된 예약이 있으므로 상품을 삭제할 수 없습니다."),
    EXIST_RESERVATION(5001, "이미 예약 신청한 상품입니다."),
    RESERVATION_NOT_FOUND(5002, "찾을 수 없는 예약입니다."),
    RESERVATION_NOT_PENDING(5003, "수정할 수 없는 예약입니다."),
    RESERVATION_NOT_CONFIRMED(5004, "확정된 예약이 아닙니다."),
    RESERVATION_EXIST_CHECK(5005, "이미 거래 완료 요청을 보냈습니다."),
    RESERVATION_INVALID_BUYER(5006, "본인의 상품은 구매할 수 없습니다."),
    NOT_ENOUGH_POINT(6001, "보유 잔액이 부족합니다."),
    INVALID_POINT(6002, "잘못된 금액입니다."),
    UNAUTHORIZED_ACTION(7001, "권한이 없습니다.");

    private final int code;
    private final String message;
}
