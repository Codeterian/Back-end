package com.codeterian.common.infrastructure.dto.performance;

import java.util.List;
import java.util.UUID;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;

public record PerformanceDecreaseStockRequestDto(

	UUID performanceId,
	UUID orderId,
	Long userId,
	List<TicketAddRequestDto> ticketAddRequestDtoList
) {

}
