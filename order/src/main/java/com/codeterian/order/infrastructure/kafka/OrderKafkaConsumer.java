package com.codeterian.order.infrastructure.kafka;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.codeterian.order.application.OrderService;
import com.codeterian.order.domain.entity.status.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaConsumer {

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private final OrderService orderService;

	@KafkaListener(topics = "stock-is-empty", groupId = "group-a")
	public void rollbackOrder(final String orderId){
		log.info("stock-is-empty get Message : {} ",orderId);
		orderService.modifyOrderStatus(UUID.fromString(orderId), OrderStatus.FAILED);
	}

	@KafkaListener(topics = "order-make-approved", groupId = "group-a")
	public void handleOrderApproved(final String orderId){
		log.info("order-make-approved get Message : {} ",orderId);
		orderService.modifyOrderStatus(UUID.fromString(orderId), OrderStatus.APPROVED);
	}

}
