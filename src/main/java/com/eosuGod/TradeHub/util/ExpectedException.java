package com.eosuGod.TradeHub.util;

import lombok.Getter;

@Getter
public class ExpectedException extends RuntimeException {
    private final int code;
    private final String message;

    public ExpectedException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
