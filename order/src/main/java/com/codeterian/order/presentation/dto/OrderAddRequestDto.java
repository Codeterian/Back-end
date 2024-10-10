package com.codeterian.order.presentation.dto;

import java.util.List;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;

public record OrderAddRequestDto(

	Long userId,
	List<TicketAddRequestDto> ticketAddRequestDtoList
) {
}
