package com.codeterian.order.presentation.dto;

import java.io.Serializable;
import java.util.UUID;

import com.codeterian.order.domain.entity.order.Orders;

public record OrderAddResponseDto (

	UUID orderId,
	Long userId,
	UUID ticketId,
	Integer quantity,
	Integer money

) implements Serializable {
	public static OrderAddResponseDto fromEntity(Orders orders) {
		return new OrderAddResponseDto(
			orders.getId(),
			orders.getUserId(),
			orders.getOrderLine().getTicketId(),
			orders.getOrderLine().getQuantity(),
			orders.getOrderLine().getPrice().getValue());
	}
}
