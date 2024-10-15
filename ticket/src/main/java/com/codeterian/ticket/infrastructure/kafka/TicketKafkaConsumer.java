package com.codeterian.ticket.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.codeterian.common.infrastructure.dto.performance.PerformanceDecreaseStockRequestDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddFromPerformanceRequestDto;
import com.codeterian.ticket.application.service.TicketPerformanceService;
import com.codeterian.ticket.application.service.TicketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class TicketKafkaConsumer {

	private final TicketPerformanceService ticketPerformanceService;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = "make-ticket-from-order")
	public void decreaseStock(final String makeTicketMessage) throws JsonProcessingException {
		final TicketAddFromPerformanceRequestDto message = objectMapper.readValue(makeTicketMessage,
			TicketAddFromPerformanceRequestDto.class);
		ticketPerformanceService.addTicketFromPerformance(message);
	}
}
