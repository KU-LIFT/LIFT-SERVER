package com.kulift.lift.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// 공통
	INTERNAL_ERROR("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
	VALIDATION_ERROR("VALIDATION_ERROR", HttpStatus.BAD_REQUEST, "요청이 유효하지 않습니다."),
	UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
	FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
	NOT_FOUND("NOT_FOUND", HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

	// 사용자 관련
	USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	DUPLICATE_EMAIL("DUPLICATE_EMAIL", HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
	INVALID_TOKEN("INVALID_TOKEN", HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
	TOKEN_EXPIRED("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

	// 프로젝트 관련
	PROJECT_NOT_FOUND("PROJECT_NOT_FOUND", HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."),
	NOT_PROJECT_MEMBER("NOT_PROJECT_MEMBER", HttpStatus.FORBIDDEN, "프로젝트 멤버가 아닙니다."),
	PROJECT_ROLE_INVALID("PROJECT_ROLE_INVALID", HttpStatus.BAD_REQUEST, "프로젝트 역할이 잘못되었습니다."),
	DUPLICATE_PROJECT_KEY("DUPLICATE_PROJECT_KEY", HttpStatus.BAD_REQUEST, "이미 존재하는 프로젝트 키입니다."),

	OWNER_CANNOT_BE_CHANGED("OWNER_CANNOT_BE_CHANGED", HttpStatus.BAD_REQUEST, "OWNER 권한은 변경할 수 없습니다."),
	OWNER_CANNOT_BE_REMOVED("OWNER_CANNOT_BE_REMOVED", HttpStatus.BAD_REQUEST, "OWNER는 삭제할 수 없습니다."),
	CANNOT_CHANGE_SELF_ROLE("CANNOT_CHANGE_SELF_ROLE", HttpStatus.BAD_REQUEST, "자기 자신의 권한은 변경할 수 없습니다."),

	// 보드/컬럼/태스크 관련
	BOARD_NOT_FOUND("BOARD_NOT_FOUND", HttpStatus.NOT_FOUND, "보드를 찾을 수 없습니다."),
	DUPLICATE_TEMPLATE_NAME("DUPLICATE_TEMPLATE_NAME", HttpStatus.BAD_REQUEST, "이미 존재하는 템플릿 이름입니다."),
	COLUMN_NOT_FOUND("COLUMN_NOT_FOUND", HttpStatus.NOT_FOUND, "컬럼을 찾을 수 없습니다."),
	TASK_NOT_FOUND("TASK_NOT_FOUND", HttpStatus.NOT_FOUND, "태스크를 찾을 수 없습니다."),
	INVALID_COLUMN_MOVE("INVALID_COLUMN_MOVE", HttpStatus.BAD_REQUEST, "컬럼 이동이 유효하지 않습니다."),
	TASK_ASSIGN_ERROR("TASK_ASSIGN_ERROR", HttpStatus.BAD_REQUEST, "태스크 할당에 실패했습니다."),
	BOARD_TEMPLATE_NOT_FOUND("BOARD_TEMPLATE_NOT_FOUND", HttpStatus.NOT_FOUND, "보드 템플릿을 찾을 수 없습니다.");

	private final String code;
	private final HttpStatus status;
	private final String message;
}
