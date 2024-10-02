package com.codeterian.order.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeterian.order.domain.entity.order.Orders;
import com.codeterian.order.domain.repository.OrderRepository;
import com.codeterian.order.presentation.dto.OrderAddRequestDto;
import com.codeterian.order.presentation.dto.OrderAddResponseDto;
import com.codeterian.order.presentation.dto.OrderDetailsResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	@CachePut(cacheNames = "orderCache", key = "#result.orderId()")
	public OrderAddResponseDto addOrder(OrderAddRequestDto requestDto) {
		return OrderAddResponseDto.fromEntity(orderRepository.save(Orders.add(requestDto)));
	}

	@Cacheable(cacheNames = "orderCache", key = "args[0]")
	@Transactional(readOnly = true)
	public OrderDetailsResponseDto findOrder(UUID orderId) {
		return OrderDetailsResponseDto.fromEntity(findById(orderId));
	}


	@Cacheable(cacheNames = "orderAllCache", key = "methodName")
	@Transactional(readOnly = true)
	public List<OrderDetailsResponseDto> findOrderList() {
		return orderRepository.findAll().stream().map(OrderDetailsResponseDto::fromEntity).toList();
	}

	public Orders findById(UUID orderId){
		return orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
	}

}
