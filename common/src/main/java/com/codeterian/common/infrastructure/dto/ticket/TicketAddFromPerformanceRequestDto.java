package com.codeterian.common.infrastructure.dto.ticket;

import java.util.List;
import java.util.UUID;

public record TicketAddFromPerformanceRequestDto(

	UUID orderId,
	Long userId,
	List<TicketAddRequestDto> ticketAddRequestDtoList
) {
}
