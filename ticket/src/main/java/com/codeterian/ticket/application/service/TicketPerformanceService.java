package com.codeterian.ticket.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeterian.common.infrastructure.dto.ticket.TicketAddFromPerformanceRequestDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;
import com.codeterian.ticket.domain.model.Ticket;
import com.codeterian.ticket.domain.model.TicketStatus;
import com.codeterian.ticket.domain.repository.TicketRepository;
import com.codeterian.ticket.infrastructure.kafka.TicketKafkaProducer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketPerformanceService {

	private final TicketRepository ticketRepository;
	private final TicketKafkaProducer ticketKafkaProducer;

	@Transactional
	public void addTicketFromPerformance(TicketAddFromPerformanceRequestDto requestDto){
		for(TicketAddRequestDto dto : requestDto.ticketAddRequestDtoList()){
			ticketRepository.save(Ticket.create(dto.performanceId(), TicketStatus.BOOKING, dto.price(),
				dto.seatSection(), dto.seatNumber(), requestDto.userId(), requestDto.orderId()));
		}
		ticketKafkaProducer.handleOrderApproved(requestDto.orderId());
	}
}
