package com.codeterian.payment.presentation.controller.exception;

import org.springframework.http.HttpStatus;

import com.codeterian.common.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {

	INVALID_PAYMENT_AMOUNT(HttpStatus.INTERNAL_SERVER_ERROR, "Not same payment amount and order amount"),
	FAILED_PAYMENT(HttpStatus.INTERNAL_SERVER_ERROR, "Payment failed");
	private final HttpStatus httpStatus;
	private final String message;
}
