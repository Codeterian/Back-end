package com.codeterian.order.infrastructure.kafka;

import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.codeterian.common.infrastructure.dto.payment.PaymentBeforeValidateRequestDto;
import com.codeterian.common.infrastructure.dto.performance.PerformanceDecreaseStockRequestDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;
import com.codeterian.common.infrastructure.enums.OrderStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaProducer {

	private static final String DECREASE_STOCK_TOPIC = "decrease-stock";

	private static final String PAYMENT_VALIDATE_PREPARE = "payment-validate-prepare";

	private static final String TICKET_COMPLETED = "ticket-completed";

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void decreaseStock(final UUID performanceId, final UUID orderId, final Long userId,
		List<TicketAddRequestDto> ticketAddRequestDtoList) throws JsonProcessingException {
		PerformanceDecreaseStockRequestDto decreaseStockRequestDto = new PerformanceDecreaseStockRequestDto(
			performanceId, orderId, userId, ticketAddRequestDtoList);
		kafkaTemplate.send(DECREASE_STOCK_TOPIC, objectMapper.writeValueAsString(decreaseStockRequestDto));
	}

	public void paymentValidatePrepare(final UUID orderId, final Integer price, final OrderStatus orderStatus) throws
		JsonProcessingException {
		PaymentBeforeValidateRequestDto paymentBeforeValidateRequestDto = new PaymentBeforeValidateRequestDto(
			orderId, price, orderStatus);
		log.info("payment-validate-prepare message : {} ",
			objectMapper.writeValueAsString(paymentBeforeValidateRequestDto));
		kafkaTemplate.send(PAYMENT_VALIDATE_PREPARE, objectMapper.writeValueAsString(paymentBeforeValidateRequestDto));
	}

	public void ticketCompleted(final UUID orderId) throws JsonProcessingException {
		log.info("ticket-completed message : {} ",  orderId.toString());
		kafkaTemplate.send(TICKET_COMPLETED, orderId.toString());
	}
}
