package com.codeterian.common.infrastructure.dto.ticket;

public record TicketAddFromOrdersResponseDto(

	Integer totalPrice

) {
	public static TicketAddFromOrdersResponseDto create(Integer totalPrice){
		return new TicketAddFromOrdersResponseDto(totalPrice);
	}
}
