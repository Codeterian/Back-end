package com.codeterian.common.infrastructure.dto.ticket;

import java.util.UUID;


public record TicketAddRequestDto(
        UUID performanceId,
        Integer price,
        String seatSection,
        Integer seatNumber,
		UUID orderId
) {
	public static TicketAddRequestDto fromDto(UUID performanceId, Integer price, String seatSection, Integer seatNumber, UUID orderId) {
		return new TicketAddRequestDto(performanceId, price, seatSection, seatNumber, orderId);
	}

}
