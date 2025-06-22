package com.kulift.lift.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		log.error(ex.getMessage(), ex);
		return ResponseEntity
			.status(errorCode.getStatus())
			.body(ErrorResponse.of(errorCode));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseEntity
			.status(ErrorCode.VALIDATION_ERROR.getStatus())
			.body(ErrorResponse.of(ErrorCode.VALIDATION_ERROR));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnhandledException(Exception ex) {
		log.error(ex.getMessage(), ex);
		return ResponseEntity
			.status(ErrorCode.INTERNAL_ERROR.getStatus())
			.body(ErrorResponse.of(ErrorCode.INTERNAL_ERROR));
	}
}
