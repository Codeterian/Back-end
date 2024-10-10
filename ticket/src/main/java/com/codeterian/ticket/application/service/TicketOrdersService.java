package com.codeterian.ticket.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddFromOrdersRequestDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddFromOrdersResponseDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.ticket.domain.model.Ticket;
import com.codeterian.ticket.domain.model.TicketStatus;
import com.codeterian.ticket.domain.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketOrdersService {

	private final TicketRepository ticketRepository;

	@Transactional
	public TicketAddFromOrdersResponseDto addTicketFromOrders(Long userId, TicketAddFromOrdersRequestDto requestDto){
		Integer totalPrice = 0;
		for(TicketAddRequestDto dto : requestDto.ticketAddRequestDtoList()){
			ticketRepository.save(Ticket.create(dto.performanceId(), TicketStatus.BOOKING, dto.price(),
				dto.seatSection(), dto.seatNumber(), userId, requestDto.orderId()));
			totalPrice += dto.price();
		}
		return TicketAddFromOrdersResponseDto.create(totalPrice);
	}
}
