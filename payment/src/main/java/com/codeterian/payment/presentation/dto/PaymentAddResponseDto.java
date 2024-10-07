package com.codeterian.payment.presentation.dto;

import java.util.UUID;

import com.codeterian.payment.domain.entity.enums.PaymentType;
import com.codeterian.payment.domain.entity.payment.Payment;

public record PaymentAddResponseDto(
	UUID paymentId,
	UUID orderId,
	PaymentType paymentType
) {

	public static PaymentAddResponseDto fromEntity(Payment payment) {
		return new PaymentAddResponseDto(payment.getId(), payment.getOrderId(), payment.getType());
	}

}
