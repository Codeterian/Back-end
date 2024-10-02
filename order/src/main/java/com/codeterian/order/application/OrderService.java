package com.codeterian.order.application;

import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.codeterian.order.domain.entity.order.Orders;
import com.codeterian.order.domain.repository.OrderRepository;
import com.codeterian.order.presentation.dto.OrderAddRequestDto;
import com.codeterian.order.presentation.dto.OrderAddResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	@CachePut(cacheNames = "orderCache", key = "args[0].userId")
	public OrderAddResponseDto addOrder(OrderAddRequestDto requestDto) {
		return OrderAddResponseDto.fromEntity(orderRepository.save(Orders.add(requestDto)));
	}



}
