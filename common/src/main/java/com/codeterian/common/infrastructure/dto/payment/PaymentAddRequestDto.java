package com.codeterian.common.infrastructure.dto.payment;

import java.util.UUID;

import com.codeterian.common.infrastructure.entity.enums.PaymentType;
public record PaymentAddRequestDto(
	UUID orderId,
	PaymentType paymentType
) {
}
