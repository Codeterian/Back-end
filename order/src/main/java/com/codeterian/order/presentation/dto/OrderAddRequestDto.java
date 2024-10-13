package com.codeterian.order.presentation.dto;

import java.util.List;
import java.util.UUID;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;

public record OrderAddRequestDto(

	Long userId,
	UUID performanceId,

	Integer totalPrice,
	List<TicketAddRequestDto> ticketAddRequestDtoList
) {
}
