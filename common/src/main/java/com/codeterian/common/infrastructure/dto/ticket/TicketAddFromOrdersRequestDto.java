package com.codeterian.common.infrastructure.dto.ticket;

import java.util.List;
import java.util.UUID;

public record TicketAddFromOrdersRequestDto(

	UUID orderId,
	List<TicketAddRequestDto> ticketAddRequestDtoList
) {
	public static TicketAddFromOrdersRequestDto create(UUID orderId, List<TicketAddRequestDto> ticketAddRequestDtoList){
		return new TicketAddFromOrdersRequestDto(orderId, ticketAddRequestDtoList);
	}
}
