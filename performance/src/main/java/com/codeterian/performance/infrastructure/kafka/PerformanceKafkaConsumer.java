package com.codeterian.performance.infrastructure.kafka;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.codeterian.common.infrastructure.dto.performance.PerformanceDecreaseStockRequestDto;
import com.codeterian.performance.application.PerformanceService;
import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.performance.PerformanceDocument;
import com.codeterian.performance.domain.repository.PerformanceDocumentRepository;
import com.codeterian.performance.domain.repository.PerformanceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PerformanceKafkaConsumer {

	private final PerformanceService performanceService;
	private final PerformanceRepository performanceRepository;
	private final PerformanceDocumentRepository performanceDocumentRepository;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = "decrease-stock")
	public void decreaseStock(final String decreaseStockMessage) throws JsonProcessingException {
		final PerformanceDecreaseStockRequestDto message = objectMapper.readValue(decreaseStockMessage,
			PerformanceDecreaseStockRequestDto.class);
		performanceService.modifyStock(message);
	}

	@KafkaListener(topics = "performance_topic", groupId = "performance_group")
	public void listen(String performanceId) {
		log.info("Received performance id: {}", performanceId);

		// performanceId를 UUID로 변환
		UUID id = UUID.fromString(performanceId);

		// performanceId를 사용하여 Performance 객체 조회
		Performance performance = performanceRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

		// Elasticsearch에 저장
		performanceDocumentRepository.save(PerformanceDocument.from(performance));
	}
}
