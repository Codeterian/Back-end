package com.codeterian.common.exception;

import java.util.List;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

	private final String code;
	private final String message;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private final List<ValidationError> errors;

	// @valid 사용할 떄 어느 필드에 에러가 발생하는지 알기 위해 추가
	@Getter
	@Builder
	@RequiredArgsConstructor
	public static class ValidationError {

		private final String field;
		private final String message;

		public static ValidationError of(final FieldError fieldError) {
			return ValidationError.builder()
				.field(fieldError.getField())
				.message(fieldError.getDefaultMessage())
				.build();
		}
	}

}
