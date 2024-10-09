package com.codeterian.order.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.order.application.OrderService;
import com.codeterian.order.presentation.dto.OrderAddRequestDto;
import com.codeterian.order.presentation.dto.OrderAddResponseDto;
import com.codeterian.order.presentation.dto.OrderDetailsResponseDto;
import com.codeterian.order.presentation.dto.OrderModifyRequestDto;
import com.codeterian.order.presentation.dto.OrderModifyResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseDto<OrderAddResponseDto> orderAdd(
		@RequestBody OrderAddRequestDto requestDto
	){
		return ResponseDto.okWithData(orderService.addOrder(requestDto));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDetailsResponseDto> orderDetails(
		@PathVariable UUID orderId
	){
		return ResponseEntity.ok(orderService.findOrder(orderId));
	}

	@GetMapping
	public ResponseEntity<List<OrderDetailsResponseDto>> orderList(){
		return ResponseEntity.ok(orderService.findOrderList());
	}

	@PatchMapping("/{orderId}")
	public ResponseEntity<OrderModifyResponseDto> orderModify(
		@PathVariable UUID orderId,
		@RequestBody OrderModifyRequestDto requestDto
	){
		return ResponseEntity.ok(orderService.modifyOrder(orderId, requestDto));
	}
	//
	// @DeleteMapping("/orderId")
	// public ResponseEntity<> orderRemove(
	// 	@PathVariable UUID orderId
	// ){
	// 	orderService.removeOrder(orderId);
	// 	return ResponseEntity.ok()
	// }
}
