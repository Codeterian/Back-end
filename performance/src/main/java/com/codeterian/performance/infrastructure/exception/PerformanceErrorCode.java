package com.codeterian.performance.infrastructure.exception;

import org.springframework.http.HttpStatus;

import com.codeterian.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PerformanceErrorCode implements ErrorCode {

	FORBIDDEN_ADMIN_ACCESS(HttpStatus.FORBIDDEN, "관리자 회원이 아닙니다."),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
	PERFORMANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공연입니다."),
	DUPLICATE_PERFORMANCE_TITLE(HttpStatus.CONFLICT, "중복되는 공연입니다."),
	IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
	INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 이전이어야 합니다."),
	INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, "잘못된 pageNumber 요청입니다."),
	DATABASE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 저장에 실패했습니다."),
	INVALID_PAGE_SIZE(HttpStatus.BAD_REQUEST, "잘못된 pageSize 요청입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
