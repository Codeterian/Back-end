package com.codeterian.performance.application;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	private final KafkaTemplate<String, String> kafkaTemplate;

	public void sendPerformanceToKafka(UUID performanceId) {
		log.info("Sending performance to Kafka topic {}", performanceId);
		kafkaTemplate.send("performance_topic",performanceId.toString());
	}
}
