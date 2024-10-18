package com.codeterian.payment.infrastructure.config.kafka;

import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddFromPerformanceRequestDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentKafkaProducer {

	private static final String MAKE_TICKET = "prepare-payment";
	private static final String STOCK_IS_EMPTY = "stock-is-empty";

	private static final String SUCCESS_PAYMENT_TO_ORDER = "success-payment-to-order";


	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void makeTicket(final UUID orderId, final Long userId, final List<TicketAddRequestDto> ticketAddRequestDtoList) throws JsonProcessingException {
		TicketAddFromPerformanceRequestDto ticketAddFromPerformanceRequestDto = new TicketAddFromPerformanceRequestDto(
			orderId, userId, ticketAddRequestDtoList);
		log.info("Make ticket to Kafka Message {} : ", ticketAddFromPerformanceRequestDto);
		kafkaTemplate.send(MAKE_TICKET, objectMapper.writeValueAsString(ticketAddFromPerformanceRequestDto));
	}

	public void sendPerformanceToKafka(UUID performanceId) {
		log.info("Sending performance to Kafka topic {}", "performance_topic");
		kafkaTemplate.send("performance_topic",performanceId.toString());
	}

	public void successPaymentToOrder(UUID orderId){
		log.info("Sending success payment to order with orderId : {} ", orderId.toString());
		kafkaTemplate.send(SUCCESS_PAYMENT_TO_ORDER, orderId.toString());
	}

}
