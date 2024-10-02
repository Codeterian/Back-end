package com.codeterian.ticket.application.service;

import com.codeterian.ticket.domain.repository.TicketRepository;
import com.codeterian.ticket.presentation.dto.request.TicketAddRequestDto;
import com.codeterian.ticket.presentation.dto.request.TicketModifyRequestDto;
import com.codeterian.ticket.presentation.dto.response.TicketFindResponseDto;
import com.codeterian.ticket.presentation.dto.response.TicketModifyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public void addTicket(TicketAddRequestDto requestDto) {

    }

    public TicketFindResponseDto findTicketById(UUID ticketId) {
        return null;
    }

    public List<TicketFindResponseDto> findAllTicket() {
        return null;
    }

    public TicketModifyResponseDto modifyTicket(UUID ticketId, TicketModifyRequestDto requestDto) {
        return null;
    }

    public void deleteTicketById(UUID ticketId) {

    }

}
