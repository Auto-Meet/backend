package com.AutoMeet.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(HttpServletRequest request, ServiceException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.info(String.valueOf(request), e, errorCode);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.error(errorCode));
    }
}
