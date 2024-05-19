package com.AutoMeet.domain.meet.exception;

import com.AutoMeet.global.exception.ErrorCode;
import com.AutoMeet.global.exception.ServiceException;

public class NotYourMeetingException extends ServiceException {

    private static final ErrorCode ERROR_CODE = ErrorCode.NOT_YOUR_MEETING;
    private static final String MESSAGE_KEY = "사용자가 참여한 회의가 아닙니다.";

    public NotYourMeetingException(String meetingId) {
        super(ERROR_CODE, MESSAGE_KEY, new Object[] {meetingId});
    }
}
