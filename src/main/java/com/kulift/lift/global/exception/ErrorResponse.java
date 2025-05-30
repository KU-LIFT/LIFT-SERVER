package com.kulift.lift.global.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
	private final String code;
	private final String message;

	private ErrorResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
	}
}
