package com.codeterian.payment.presentation.dto;

import java.util.UUID;

import com.codeterian.payment.domain.entity.enums.PaymentType;

public record PaymentAddRequestDto(
	UUID orderId,
	PaymentType paymentType
) {
}
