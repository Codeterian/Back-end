package com.codeterian.ticket.presentation.dto.response;

import com.codeterian.ticket.domain.model.Ticket;
import com.codeterian.ticket.domain.model.TicketStatus;

import java.util.UUID;

public record TicketModifyResponseDto(

        UUID ticketId,
        UUID performanceId,
        int seatNumber,
        String seatSection,
        Integer price,
        TicketStatus ticketStatus
) {

    public static TicketModifyResponseDto fromEntity(Ticket ticket) {
        return new TicketModifyResponseDto(ticket.getId(), ticket.getPerformanceId(), ticket.getSeatNumber(),
                ticket.getSeatSection(), ticket.getPrice(), ticket.getTicketStatus());
    }

}
