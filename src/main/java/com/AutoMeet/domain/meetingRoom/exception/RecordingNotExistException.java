package com.AutoMeet.domain.meetingRoom.exception;

import com.AutoMeet.global.exception.ErrorCode;
import com.AutoMeet.global.exception.ServiceException;

public class RecordingNotExistException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.RECORDING_NOT_EXIST;
    private static final String MESSAGE_KEY = "녹화가 진행되지 않고 있습니다.";

    public RecordingNotExistException(String meetingId) {
        super(ERROR_CODE, MESSAGE_KEY, new Object[] {meetingId});
    }
}
