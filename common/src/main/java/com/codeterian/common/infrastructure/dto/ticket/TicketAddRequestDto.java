package com.codeterian.common.infrastructure.dto.ticket;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TicketAddRequestDto(


	@NotNull(message = "performanceId is not Null")
	UUID performanceId,
	@NotNull(message = "price is not Null")
	Integer price,
	@NotNull(message = "seatSection is not Null")
	String seatSection,
	@NotNull(message = "seatNumber is not Null")
	Integer seatNumber,
	@NotNull(message = "orderId is not Null")
	UUID orderId
) {
	public static TicketAddRequestDto fromDto(UUID performanceId, Integer price, String seatSection, Integer seatNumber,
		UUID orderId) {
		return new TicketAddRequestDto(performanceId, price, seatSection, seatNumber, orderId);
	}

}
