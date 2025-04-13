package com.kulift.lift.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_TOKEN("INVALID_TOKEN", HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    VALIDATION_ERROR("VALIDATION_ERROR", HttpStatus.BAD_REQUEST, "요청이 유효하지 않습니다."),
    INTERNAL_ERROR("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
