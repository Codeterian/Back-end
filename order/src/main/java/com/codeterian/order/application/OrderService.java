package com.codeterian.order.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.payment.PaymentAddRequestDto;
import com.codeterian.common.infrastructure.entity.enums.PaymentType;
import com.codeterian.order.domain.entity.order.Orders;
import com.codeterian.order.domain.repository.OrderRepository;
import com.codeterian.order.infrastructure.client.PaymentClient;
import com.codeterian.order.presentation.dto.OrderAddRequestDto;
import com.codeterian.order.presentation.dto.OrderAddResponseDto;
import com.codeterian.order.presentation.dto.OrderDetailsResponseDto;
import com.codeterian.order.presentation.dto.OrderModifyRequestDto;
import com.codeterian.order.presentation.dto.OrderModifyResponseDto;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final RedisTemplate<String, Orders> redisTemplate;
	private final PaymentClient paymentClient;

	//Write - Through 전략
	@Transactional
	public OrderAddResponseDto addOrder(OrderAddRequestDto requestDto) {
		Orders orders = orderRepository.save(Orders.add(requestDto));

		// 2.Redis에 Orders 엔티티를 저장
		String redisKey = "orderCache::"+orders.getId();
		redisTemplate.opsForValue().set(redisKey, orders);
		PaymentAddRequestDto paymentAddRequestDto = new PaymentAddRequestDto(orders.getId(), PaymentType.TOSS);

		ResponseDto responseDto = paymentClient.paymentAdd(paymentAddRequestDto);

		if(responseDto.getCode() == 200)
			orders.success();

		return OrderAddResponseDto.fromEntity(orders);
	}

	// Read-Through 전략
	@Transactional(readOnly = true)
	public OrderDetailsResponseDto findOrder(UUID orderId) {
		String redisKey = "orderCache::"+ orderId;
		Orders orders = redisTemplate.opsForValue().get(redisKey);

		if(orders == null){
			orders = findById(orderId);
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
	public OrderModifyResponseDto modifyOrder(UUID orderId, OrderModifyRequestDto requestDto) {
		Orders orders = findById(orderId);
		if(orders.getUserId().equals(requestDto.userId())){
			throw new IllegalArgumentException();
		}
		orders.update(requestDto);
		redisTemplate.opsForValue().set("orderCache::"+ orderId, orders);

		return OrderModifyResponseDto.fromEntity(orders);
	}
	//
	// @CacheEvict(cacheNames = {"orderAllCache", "orderCache"}, allEntries = true)
	// @Transactional
	// public void removeOrder(UUID orderId) {
	// 	Orders orders = findById(orderId);
	// 	orders.delete();
	// }

	public Orders findById(UUID orderId){
		return orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
	}

}
