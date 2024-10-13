package com.codeterian.ticket.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddFromPerformanceRequestDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddFromOrdersResponseDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;
import com.codeterian.ticket.domain.model.Ticket;
import com.codeterian.ticket.domain.model.TicketStatus;
import com.codeterian.ticket.domain.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketPerformanceService {

	private final TicketRepository ticketRepository;

	@Transactional
	public void addTicketFromPerformance(TicketAddFromPerformanceRequestDto requestDto){
		for(TicketAddRequestDto dto : requestDto.ticketAddRequestDtoList()){
			ticketRepository.save(Ticket.create(dto.performanceId(), TicketStatus.BOOKING, dto.price(),
				dto.seatSection(), dto.seatNumber(), requestDto.userId(), requestDto.orderId()));
		}
	}
}
