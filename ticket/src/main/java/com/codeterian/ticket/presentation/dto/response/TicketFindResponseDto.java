package com.codeterian.ticket.presentation.dto.response;

import com.codeterian.ticket.domain.model.Ticket;
import com.codeterian.ticket.domain.model.TicketStatus;

import java.util.UUID;

public record TicketFindResponseDto(
        UUID ticketId,
        UUID performanceId,
        int seatNumber,
        String seatSection,
        Integer price,
        TicketStatus ticketStatus
) {

    public static TicketFindResponseDto fromEntity(Ticket ticket) {
        return new TicketFindResponseDto(ticket.getId(), ticket.getPerformanceId(), ticket.getSeatNumber(),
                ticket.getSeatSection(), ticket.getPrice(), ticket.getTicketStatus());
    }

}
