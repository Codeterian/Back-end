package com.codeterian.payment.infrastructure.config.kafka;

import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.codeterian.common.infrastructure.dto.payment.PaymentBeforeValidateRequestDto;
import com.codeterian.common.infrastructure.enums.OrderStatus;
import com.codeterian.payment.application.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siot.IamportRestClient.exception.IamportResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentKafkaConsumer {

	private final ObjectMapper objectMapper;
	private final PaymentService paymentService;

	@KafkaListener(topics = "payment-validate-prepare")
	public void paymentBeforeValidate(final String paymentBeforeValidateMessage) throws
		IOException,
		IamportResponseException {
		final PaymentBeforeValidateRequestDto message = objectMapper.readValue(paymentBeforeValidateMessage,
			PaymentBeforeValidateRequestDto.class);
		if (message.orderStatus() == OrderStatus.APPROVED) {
			paymentService.paymentBeforeValidate(message);
		}
		// 롤백 함수
		else{

		}
	}
}
