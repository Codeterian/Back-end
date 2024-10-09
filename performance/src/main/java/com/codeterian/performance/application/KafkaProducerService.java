package com.codeterian.performance.application;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	private final KafkaTemplate<String, String> kafkaTemplate;

	public void sendPerformanceToKafka(UUID performanceId) {
		kafkaTemplate.send("performance_topic",performanceId.toString());
	}
}
