package com.codeterian.payment.domain.entity.payment;

import java.util.UUID;

import com.codeterian.payment.domain.entity.enums.PaymentStatus;
import com.codeterian.payment.domain.entity.enums.PaymentType;
import com.codeterian.payment.presentation.dto.PaymentAddRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private UUID orderId;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;


	@Enumerated(EnumType.STRING)
	private PaymentType type;

	public static Payment add(PaymentAddRequestDto requestDto) {
		return Payment.builder()
			.orderId(requestDto.orderId())
			.status(PaymentStatus.PENDING)
			.type(requestDto.paymentType())
			.build();
	}
}
