package com.AutoMeet.domain.meetingRoom.exception;

import com.AutoMeet.global.exception.ErrorCode;
import com.AutoMeet.global.exception.ServiceException;

public class MeetingNotExistException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MEETING_NOT_EXIST;
    private static final String MESSAGE_KEY = "회의실이 존재하지 않습니다.";

    public MeetingNotExistException(String meetingId) {
        super(ERROR_CODE, MESSAGE_KEY, new Object[] {meetingId});
    }
}
