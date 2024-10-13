package com.codeterian.order.presentation.dto;

import java.io.Serializable;
import java.util.UUID;

import com.codeterian.order.domain.entity.order.Orders;

public record OrderModifyResponseDto(
	UUID orderId,
	Long userId,
	String orderStatus

) implements Serializable {
	public static OrderModifyResponseDto fromEntity(Orders order) {
		return new OrderModifyResponseDto(
			order.getId(),
			order.getUserId(),
			order.getOrderStatus().toString()
		);
	}
}
