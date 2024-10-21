package com.codeterian.payment.domain.entity.payment;

import java.util.UUID;

import com.codeterian.common.infrastructure.entity.BaseEntity;
import com.codeterian.common.infrastructure.enums.PaymentStatus;

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
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private UUID orderId;

	private Integer price;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	public static Payment create(UUID orderId, Integer price) {
		return Payment.builder()
			.orderId(orderId)
			.price(price)
			.status(PaymentStatus.PENDING)
			.build();
	}

	public void updateStatus(PaymentStatus paymentStatus) {
		this.status = paymentStatus;
	}
}
