package com.codeterian.order.presentation.dto;

import java.util.List;
import java.util.UUID;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;

import jakarta.validation.constraints.NotNull;

public record OrderAddRequestDto(

	@NotNull(message = "userId is not Null")
	Long userId,
	@NotNull(message = "performanceId is not Null")
	UUID performanceId,

	@NotNull(message = "totalPrice is not Null")
	Integer totalPrice,
	List<TicketAddRequestDto> ticketAddRequestDtoList
) {
}
