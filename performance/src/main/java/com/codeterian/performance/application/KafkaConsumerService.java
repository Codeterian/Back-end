package com.codeterian.performance.application;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.performance.PerformanceDocument;
import com.codeterian.performance.domain.repository.PerformanceRepository;
import com.codeterian.performance.infrastructure.persistence.PerformanceDocumentRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

	private final PerformanceDocumentRepositoryImpl performanceDocumentRepository;
	private final PerformanceRepository performanceRepository;

	@KafkaListener(topics = "performance_topic", groupId = "performance_group")
	public void listen(String performanceId) {
		// performanceId를 UUID로 변환
		UUID id = UUID.fromString(performanceId);

		// performanceId를 사용하여 Performance 객체 조회
		Performance performance = performanceRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

		// Elasticsearch에 저장
		performanceDocumentRepository.save(PerformanceDocument.from(performance));
	}

}
