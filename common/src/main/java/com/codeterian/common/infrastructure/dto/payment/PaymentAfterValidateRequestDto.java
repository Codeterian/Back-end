package com.codeterian.common.infrastructure.dto.payment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PaymentAfterValidateRequestDto(
	String paymentUid,
	String orderUid,
	String price

) {

}
