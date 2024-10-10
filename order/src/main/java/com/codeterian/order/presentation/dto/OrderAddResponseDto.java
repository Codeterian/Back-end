package com.codeterian.order.presentation.dto;

import java.io.Serializable;
import java.util.UUID;

import com.codeterian.order.domain.entity.order.Orders;

public record OrderAddResponseDto (

	UUID orderId,
	Long userId

) implements Serializable {
	public static OrderAddResponseDto fromEntity(Orders orders) {
		return new OrderAddResponseDto(
			orders.getId(),
			orders.getUserId());
	}
}
