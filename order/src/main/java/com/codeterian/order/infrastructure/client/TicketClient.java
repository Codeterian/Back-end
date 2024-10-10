package com.codeterian.order.infrastructure.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddFromOrdersRequestDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddFromOrdersResponseDto;
import com.codeterian.common.infrastructure.dto.ticket.TicketAddRequestDto;
import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.order.infrastructure.config.FeignConfig;

@FeignClient(value = "ticket",url = "http://localhost:19094", path = "/api/v1/tickets", configuration = FeignConfig.class)
public interface TicketClient {

	@PostMapping("/fromOrders")
	ResponseEntity<TicketAddFromOrdersResponseDto> addTicketFromOrders(
		@RequestHeader("X-User-Id") Long userId,
		@RequestBody TicketAddFromOrdersRequestDto requestDto);

}
