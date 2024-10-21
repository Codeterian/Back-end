package com.codeterian.common.infrastructure.dto.payment;

import java.util.UUID;

import com.codeterian.common.infrastructure.enums.OrderStatus;

public record PaymentBeforeValidateRequestDto(

	UUID orderId,
	Integer price,

	OrderStatus orderStatus

) {

}
