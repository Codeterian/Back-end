package com.codeterian.payment.presentation.dto;

import java.util.UUID;

import com.codeterian.payment.domain.entity.enums.PaymentType;
import com.codeterian.payment.domain.entity.payment.Payment;

public record PaymentDetailsResponseDto(
	UUID paymentId,
	UUID orderId,
	PaymentType paymentType
) {
	public static PaymentDetailsResponseDto fromEntity(Payment payment) {
		return new PaymentDetailsResponseDto(payment.getId(), payment.getOrderId(), payment.getType());
	}
}
