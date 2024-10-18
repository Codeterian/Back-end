package com.codeterian.order.application;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeterian.common.exception.CommonErrorCode;
import com.codeterian.common.exception.RestApiException;
import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.order.domain.entity.order.Orders;
import com.codeterian.common.infrastructure.enums.OrderStatus;
import com.codeterian.order.domain.repository.OrderRepository;
import com.codeterian.order.infrastructure.kafka.OrderKafkaProducer;
import com.codeterian.order.infrastructure.redisson.aspect.DistributedLock;
import com.codeterian.order.presentation.dto.OrderAddRequestDto;
import com.codeterian.order.presentation.dto.OrderAddResponseDto;
import com.codeterian.order.presentation.dto.OrderDetailsResponseDto;
import com.codeterian.order.presentation.dto.OrderModifyRequestDto;
import com.codeterian.order.presentation.dto.OrderModifyResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final OrderRepository orderRepository;
	private final RedisTemplate<String, Orders> redisTemplate;
	private final OrderKafkaProducer orderKafkaProducer;

	//Write - Through 전략
	@DistributedLock(key = "#lockName")
	public OrderAddResponseDto addOrder(String lockName, Passport passport, OrderAddRequestDto requestDto) throws
		JsonProcessingException {

		// 0. 권한 체크
		if(passport.getUserRole() != UserRole.CUSTOMER){
			throw new RestApiException(CommonErrorCode.UNAUTHORIZED_USER);
		}

		// 1. 주문 생성
		Orders orders = Orders.add(requestDto.totalPrice(), requestDto.userId());
		orders.createBy(requestDto.userId());
		orders.updateBy(requestDto.userId());
		orderRepository.save(orders);

		// 2. 공연장 재고 감소
		orderKafkaProducer.decreaseStock(requestDto.performanceId(), orders.getId(), requestDto.userId(),
			requestDto.ticketAddRequestDtoList());

		// 3. Redis에 Orders 엔티티를 저장
		String redisKey = "orderCache::" + orders.getId();
		redisTemplate.opsForValue().set(redisKey, orders);

		return OrderAddResponseDto.fromEntity(orders);
	}

	// Read-Through 전략
	@Transactional(readOnly = true)
	public OrderDetailsResponseDto findOrder(Passport passport, UUID orderId) {
		String redisKey = "orderCache::" + orderId;
		Orders orders = redisTemplate.opsForValue().get(redisKey);

		if (orders == null) {
			orders = findById(orderId);
			if (!orders.getUserId().equals(passport.getUserId())) {
				throw new RestApiException(CommonErrorCode.UNAUTHORIZED_USER);
			}
			redisTemplate.opsForValue().set(redisKey, orders);
		}

		return OrderDetailsResponseDto.fromEntity(orders);
	}

	@Cacheable(cacheNames = "orderAllCache", key = "methodName")
	@Transactional(readOnly = true)
	public List<OrderDetailsResponseDto> findOrderList() {
		return orderRepository.findAll().stream().map(OrderDetailsResponseDto::fromEntity).toList();
	}

	@CacheEvict(cacheNames = "orderAllCache", allEntries = true)
	@Transactional
	public OrderModifyResponseDto modifyOrder(UUID orderId, Passport passport, OrderModifyRequestDto requestDto) {
		Orders orders = findById(orderId);
		if(passport.getUserRole() == UserRole.CUSTOMER)
			throw new RestApiException(CommonErrorCode.UNAUTHORIZED_USER);

		orders.updateMoney(requestDto.price());

		redisTemplate.opsForValue().set("orderCache::" + orderId, orders);
		return OrderModifyResponseDto.fromEntity(orders);
	}

	@CacheEvict(cacheNames = {"orderAllCache", "orderCache"}, allEntries = true)
	@Transactional
	public void removeOrder(Passport passport, UUID orderId) {
		Orders orders = findById(orderId);
		if(!orders.getUserId().equals(passport.getUserId())){
			throw new RestApiException(CommonErrorCode.UNAUTHORIZED_USER);
		}
		orders.delete(orders.getUserId());
	}

	@Transactional
	public void deleteOrder(UUID orderId) {
		orderRepository.deleteById(orderId);
	}


	@Transactional
	public void approvedOrderStatus(UUID orderId, OrderStatus orderStatus) throws JsonProcessingException {
		Orders order = findById(orderId);
		order.updateStatus(orderStatus);
		orderKafkaProducer.paymentValidatePrepare(order.getId(), order.getTotalPrice().getValue(), orderStatus);
	}


	@Transactional
	public void failedOrderStatus(UUID orderId, OrderStatus orderStatus) {
		Orders order = findById(orderId);
		order.updateStatus(orderStatus);
	}


	@Transactional
	public void completedOrderStatus(UUID orderId, OrderStatus orderStatus) {
		Orders order = findById(orderId);
		order.updateStatus(orderStatus);
	}


	public Orders findById(UUID orderId) {
		return orderRepository.findByIdAndIsDeletedFalse(orderId).orElseThrow(
			() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));
	}

}
