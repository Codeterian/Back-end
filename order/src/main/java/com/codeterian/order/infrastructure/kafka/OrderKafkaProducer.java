package com.codeterian.order.infrastructure.kafka;

import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.codeterian.common.infrastructure.dto.performance.PerformanceDecreaseStockRequestDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderKafkaProducer {

	private static final String DECREASE_STOCK_TOPIC = "decrease-stock";

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void decreaseStock(final UUID performanceId, final UUID orderId, final Long userId,
		List<TicketAddRequestDto> ticketAddRequestDtoList) throws JsonProcessingException {
		PerformanceDecreaseStockRequestDto decreaseStockRequestDto = new PerformanceDecreaseStockRequestDto(
			performanceId, orderId, userId, ticketAddRequestDtoList);
		kafkaTemplate.send(DECREASE_STOCK_TOPIC, objectMapper.writeValueAsString(decreaseStockRequestDto));
	}

}
