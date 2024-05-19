package com.AutoMeet.domain.meetingRoom.exception;

import com.AutoMeet.global.exception.ErrorCode;
import com.AutoMeet.global.exception.ServiceException;

public class SessionNotExistException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SESSION_NOT_EXIST;
    private static final String MESSAGE_KEY = "세션이 존재하지 않습니다.";

    public SessionNotExistException(String session) {
        super(ERROR_CODE, MESSAGE_KEY, new Object[] {session});
    }
}
