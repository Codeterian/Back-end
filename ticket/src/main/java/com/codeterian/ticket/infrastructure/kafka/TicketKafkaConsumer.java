package com.codeterian.ticket.infrastructure.kafka;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddFromPerformanceRequestDto;
import com.codeterian.ticket.application.service.TicketPerformanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketKafkaConsumer {

	private final TicketPerformanceService ticketPerformanceService;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = "make-ticket-from-order")
	public void decreaseStock(final String makeTicketMessage) throws JsonProcessingException {
		final TicketAddFromPerformanceRequestDto message = objectMapper.readValue(makeTicketMessage,
			TicketAddFromPerformanceRequestDto.class);
		ticketPerformanceService.addTicketFromPerformance(message);
	}

	@KafkaListener(topics = "ticket-completed")
	public void handleTicketCompleted(final String ticketCompletedMessage) throws JsonProcessingException {
		final UUID message = UUID.fromString(ticketCompletedMessage);
		log.info("ticket Completed Get message : {} ", ticketCompletedMessage);
		ticketPerformanceService.updateTicketsStatus(message);
	}
}
