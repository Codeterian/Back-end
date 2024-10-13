package com.codeterian.performance.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.codeterian.common.infrastructure.dto.performance.PerformanceDecreaseStockRequestDto;
import com.codeterian.performance.application.PerformanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class PerformanceKafkaConsumer {

	private final PerformanceService performanceService;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = "decrease-stock")
	public void decreaseStock(final String decreaseStockMessage) throws JsonProcessingException {
		final PerformanceDecreaseStockRequestDto message = objectMapper.readValue(decreaseStockMessage,
			PerformanceDecreaseStockRequestDto.class);
		performanceService.modifyStock(message);
	}
}
