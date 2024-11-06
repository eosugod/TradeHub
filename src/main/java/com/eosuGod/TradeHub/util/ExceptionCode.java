package com.eosuGod.TradeHub.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    EXIST_USER(1001, "이미 등록된 계좌입니다.");
    private final int code;
    private final String message;
}
