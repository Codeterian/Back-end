package com.codeterian.performance.infrastructure.exception;

import org.springframework.http.HttpStatus;

import com.codeterian.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

	FORBIDDEN_CUSTOMER_ACCESS(HttpStatus.FORBIDDEN, "고객 회원이 아닙니다."),
	DUPLICATE_REVIEW(HttpStatus.CONFLICT, "이미 리뷰를 등록하셨습니다."),
	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
	UNAUTHORIZED_REVIEW_MODIFICATION(HttpStatus.FORBIDDEN, "리뷰 작성자만 수정할 수 있습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
