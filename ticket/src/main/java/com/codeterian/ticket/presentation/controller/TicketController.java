package com.codeterian.ticket.presentation.controller;

import java.util.List;
import java.util.UUID;

import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;
import com.codeterian.ticket.application.service.TicketPerformanceService;
import com.codeterian.ticket.application.service.TicketService;
import com.codeterian.ticket.presentation.dto.request.TicketModifyRequestDto;
import com.codeterian.ticket.presentation.dto.response.TicketFindResponseDto;
import com.codeterian.ticket.presentation.dto.response.TicketModifyResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    private final TicketPerformanceService ticketOrdersService;

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> ticketAdd(@RequestBody TicketAddRequestDto requestDto,
                                                       @CurrentPassport Passport passport) {
        ticketService.addTicket(requestDto, passport.getUserId(), passport);

        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<ResponseDto<TicketFindResponseDto>> ticketFind(
            @PathVariable("ticketId")UUID ticketId
    ) {
      return ResponseEntity.ok(ResponseDto.okWithData(ticketService.findTicketById(ticketId)));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<TicketFindResponseDto>>> ticketList(
            @CurrentPassport Passport passport
    ) throws IllegalAccessException {

        if (passport.getUserRole()== UserRole.CUSTOMER) {
            throw new IllegalAccessException("접근 불가");
        }

        return ResponseEntity.ok(ResponseDto.okWithData(ticketService.findAllTicket()));
    }

    @PatchMapping("/{ticketId}")
    public ResponseEntity<ResponseDto<TicketModifyResponseDto>> ticketModify(
            @PathVariable("ticketId") UUID ticketId,
            @RequestBody TicketModifyRequestDto requestDto
    ) {
        return ResponseEntity.ok(ResponseDto.okWithData(ticketService.modifyTicket(ticketId, requestDto)));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<ResponseDto<Void>> ticketDelete(
            @PathVariable("ticketId") UUID ticketId
    ) {
        ticketService.deleteTicketById(ticketId);

        return ResponseEntity.ok(ResponseDto.ok());
    }
}
