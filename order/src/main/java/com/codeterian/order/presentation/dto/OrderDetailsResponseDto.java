package com.codeterian.order.presentation.dto;

import java.io.Serializable;
import java.util.UUID;

import com.codeterian.order.domain.entity.order.Orders;

public record OrderDetailsResponseDto(

	UUID orderId,
	Long userId,
	String orderStatus

) implements Serializable {
	public static OrderDetailsResponseDto fromEntity(Orders order) {
		return new OrderDetailsResponseDto(
			order.getId(),
			order.getUserId(),
			order.getOrderStatus().toString()
		);
	}
}
