package com.codeterian.ticket.infrastructure.kafka;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketKafkaProducer {

	private static final String ORDER_PROCESSING = "order-make-approved";

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void handleOrderApproved(final UUID orderId){
		log.info("Order Processing to kafka topic : {} ", orderId);
		kafkaTemplate.send(ORDER_PROCESSING, orderId.toString());
	}

}
