package com.codeterian.order.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codeterian.order.application.OrderService;
import com.codeterian.order.presentation.dto.OrderAddRequestDto;
import com.codeterian.order.presentation.dto.OrderAddResponseDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderAddResponseDto> addOrder(
		@RequestBody OrderAddRequestDto requestDto
	){
		return ResponseEntity.ok(orderService.addOrder(requestDto));
	}
}
