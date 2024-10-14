package com.codeterian.performance.application;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.performance.PerformanceDocument;
import com.codeterian.performance.domain.repository.PerformanceDocumentRepository;
import com.codeterian.performance.domain.repository.PerformanceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

	private final PerformanceDocumentRepository performanceDocumentRepository;
	private final PerformanceRepository performanceRepository;

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
