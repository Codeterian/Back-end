package com.codeterian.ticket.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.ticket.application.feign.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;
import com.codeterian.ticket.domain.model.Ticket;
import com.codeterian.ticket.domain.model.TicketStatus;
import com.codeterian.ticket.domain.repository.TicketRepository;
import com.codeterian.ticket.presentation.dto.request.TicketModifyRequestDto;
import com.codeterian.ticket.presentation.dto.response.TicketFindResponseDto;
import com.codeterian.ticket.presentation.dto.response.TicketModifyResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserService userService;

    @Transactional
    public void addTicket(TicketAddRequestDto requestDto, Long userId, Passport passport) {

        userService.findById(userId);

        Ticket ticket = Ticket.create(requestDto.performanceId(), TicketStatus.BOOKING, requestDto.price(),
            requestDto.seatSection(), requestDto.seatNumber(), userId, requestDto.orderId());

        Optional<Ticket> existedTicket = ticketRepository.findBySeatSectionAndSeatNumberAndDeletedAtIsNull(requestDto.seatSection(),
                requestDto.seatNumber());

        if (existedTicket.isPresent()) {
            throw new IllegalStateException("예약할 수 없는 좌석입니다.");
        }

        ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public TicketFindResponseDto findTicketById(UUID ticketId) {

        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId).orElseThrow(
                //Global Exception Handler
        );
        return TicketFindResponseDto.fromEntity(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketFindResponseDto> findAllTicket() {
        return ticketRepository.findAllByDeletedAtIsNull().stream()
                .map(TicketFindResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketModifyResponseDto modifyTicket(UUID ticketId, TicketModifyRequestDto requestDto) {

        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId).orElseThrow(
                //Global Exception Handler
        );

        ticket.update(requestDto.seatSection(), requestDto.seatNumber(),
                requestDto.ticketStatus(), requestDto.price());

        return TicketModifyResponseDto.fromEntity(ticket);
    }

    @Transactional(readOnly = true)
    public void deleteTicketById(UUID ticketId) {

        Ticket ticket = ticketRepository.findByIdAndDeletedAtIsNull(ticketId).orElseThrow(
                //Global Exception Handler
        );

//        ticket.delete(userId);
        ticket.delete(1L);

    }

}
