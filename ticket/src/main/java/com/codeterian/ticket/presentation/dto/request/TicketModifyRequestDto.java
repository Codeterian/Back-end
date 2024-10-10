package com.codeterian.ticket.presentation.dto.request;

import com.codeterian.ticket.domain.model.TicketStatus;

public record TicketModifyRequestDto(
        int seatNumber,
        String seatSection,
        TicketStatus ticketStatus,
        Integer price

) {

}
