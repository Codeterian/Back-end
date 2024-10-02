package com.codeterian.order.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.codeterian.order.domain.entity.order.Orders;
import com.codeterian.order.presentation.dto.OrderDetailsResponseDto;

public interface OrderRepository {

	Orders save(Orders Order);

	Optional<Orders> findById(UUID orderId);

	List<Orders> findAll();
}
