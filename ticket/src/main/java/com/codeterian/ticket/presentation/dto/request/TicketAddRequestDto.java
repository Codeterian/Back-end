package com.codeterian.ticket.presentation.dto.request;

import com.codeterian.ticket.domain.model.TicketStatus;

import java.util.UUID;

public record TicketAddRequestDto(
        UUID performanceId,
        TicketStatus ticketStatus,
        Integer price,
        String seatSection,
        int seatNumber
) {

}
