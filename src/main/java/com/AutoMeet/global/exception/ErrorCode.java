package com.AutoMeet.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // user
    EMAIL_EXIST(HttpStatus.BAD_REQUEST, "User01", "이메일이 이미 존재합니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User02", "해당 사용자가 존재하지 않습니다."),

    // auth
    ACCESS_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Auth01", "access 토큰이 만료되었습니다."),

    // meeting
    SESSION_NOT_EXIST(HttpStatus.BAD_REQUEST, "Meeting01", "세션이 존재하지 않습니다."),
    MEETING_NOT_EXIST(HttpStatus.BAD_REQUEST, "Meeting02", "회의실이 존재하지 않습니다."),
    RECORDING_NOT_EXIST(HttpStatus.BAD_REQUEST, "Meeting03", "녹화가 진행되지 않고 있습니다."),
    NOT_YOUR_MEETING(HttpStatus.BAD_REQUEST, "Meeting04", "사용자가 참여한 회의가 아닙니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
