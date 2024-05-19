package com.AutoMeet.global.auth.exception;

import com.AutoMeet.global.exception.ErrorCode;
import com.AutoMeet.global.exception.ServiceException;

public class AccessTokenExpiredException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ACCESS_TOKEN_EXPIRED;
    private static final String MESSAGE_KEY = "access 토큰이 만료되었습니다.";

    public AccessTokenExpiredException(String message) {
        super(ERROR_CODE, MESSAGE_KEY, new Object[]{message});
    }
}
