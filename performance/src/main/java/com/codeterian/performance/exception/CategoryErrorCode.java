package com.codeterian.performance.exception;

import org.springframework.http.HttpStatus;

import com.codeterian.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {
	FORBIDDEN_ADMIN_ACCESS(HttpStatus.FORBIDDEN, "관리자 회원이 아닙니다."),
	CONFLICT_DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),
	NOT_FOUND_PARENT_CATEGORY(HttpStatus.NOT_FOUND, "상위 카테고리가 존재하지 않습니다."),
	NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
