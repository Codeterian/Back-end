package com.codeterian.queue.application.feign.dto;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;

import java.util.List;
import java.util.UUID;

public record OrderAddRequestDto(
        Long userId,
        UUID performanceId,

        Integer totalPrice,
        List<TicketAddRequestDto> ticketAddRequestDtoList

) {

}
