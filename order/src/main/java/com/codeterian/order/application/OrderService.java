package com.codeterian.order.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeterian.order.domain.entity.order.Orders;
import com.codeterian.order.domain.repository.OrderRepository;
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

	@CachePut(cacheNames = "orderCache", key = "args[0].orderId")
	@CacheEvict(cacheNames = "orderAllCache", allEntries = true)
	@Transactional
	public OrderModifyResponseDto modifyOrder(OrderModifyRequestDto requestDto) {
		Orders orders = findById(requestDto.orderId());
		if(orders.getUserId().equals(requestDto.userId())){
			throw new IllegalArgumentException();
		}
		orders.update(requestDto);
		return OrderModifyResponseDto.fromEntity(orders);
	}

	public Orders findById(UUID orderId){
		return orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
	}

}
