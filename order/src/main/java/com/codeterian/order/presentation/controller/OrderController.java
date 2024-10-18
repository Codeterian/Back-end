package com.codeterian.order.presentation.controller;

import java.util.List;
import java.util.UUID;

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
import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.order.application.OrderService;
import com.codeterian.order.presentation.dto.OrderAddRequestDto;
import com.codeterian.order.presentation.dto.OrderAddResponseDto;
import com.codeterian.order.presentation.dto.OrderDetailsResponseDto;
import com.codeterian.order.presentation.dto.OrderModifyRequestDto;
import com.codeterian.order.presentation.dto.OrderModifyResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {

	private final OrderService orderService;

	@Operation(summary = "Order Add", description = "해당 공연장에 대한 주문 진행")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "주문 성공",
		content = {@Content(schema = @Schema(implementation = OrderAddResponseDto.class))}),
		@ApiResponse(responseCode = "400", description = "주문 정보가 잘못 입력 되었습니다.")
	})
	@PostMapping
	public ResponseEntity<ResponseDto<OrderAddResponseDto>> orderAdd(
		@Parameter(hidden = true) @CurrentPassport Passport passport,
		@Valid @RequestBody OrderAddRequestDto requestDto
	) throws JsonProcessingException {
		return ResponseEntity.ok(ResponseDto.okWithData(orderService.addOrder("addOrder", passport, requestDto)));
	}

	@Operation(summary = "Order Details", description = "해당 주문에 대한 정보 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = {@Content(schema = @Schema(implementation = OrderDetailsResponseDto.class))}),
		@ApiResponse(responseCode = "404", description = "주문 정보가 잘못 입력 되었습니다."),
		@ApiResponse(responseCode = "401", description = "해당 유저는 권한이 없습니다.")
	})
	@GetMapping("/{orderId}")
	public ResponseEntity<ResponseDto<OrderDetailsResponseDto>> orderDetails(
		@Parameter(hidden = true) @CurrentPassport Passport passport,
		@PathVariable UUID orderId
	){
		return ResponseEntity.ok(ResponseDto.okWithData(orderService.findOrder(passport, orderId)));
	}

	@Operation(summary = "Total Order List", description = "전체 주문 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = {@Content(mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = OrderDetailsResponseDto.class)))}),
	})
	@GetMapping
	public ResponseEntity<ResponseDto<List<OrderDetailsResponseDto>>> orderList(){
		return ResponseEntity.ok(ResponseDto.okWithData(orderService.findOrderList()));
	}

	@Operation(summary = "Update Order Money", description = "주문 금액 변경")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = {@Content(schema = @Schema(implementation = OrderModifyResponseDto.class))}),
		@ApiResponse(responseCode = "404", description = "주문 정보가 잘못 입력 되었습니다."),
		@ApiResponse(responseCode = "401", description = "해당 유저는 권한이 없습니다.")
	})
	@PatchMapping("/{orderId}")
	public ResponseEntity<OrderModifyResponseDto> orderModify(
		@PathVariable UUID orderId,
		@Parameter(hidden = true) @CurrentPassport Passport passport,
		@Valid @RequestBody OrderModifyRequestDto requestDto
	){
		return ResponseEntity.ok(orderService.modifyOrder(orderId, passport, requestDto));
	}

	@Operation(summary = "Delete Order ", description = "주문 취소")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "취소 성공"),
		@ApiResponse(responseCode = "404", description = "주문 정보가 잘못 입력 되었습니다."),
		@ApiResponse(responseCode = "401", description = "해당 유저는 권한이 없습니다.")
	})
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ResponseDto<Void>> orderRemove(
		@Parameter(hidden = true) @CurrentPassport Passport passport,
		@PathVariable UUID orderId
	){
		orderService.removeOrder(passport, orderId);
		return ResponseEntity.ok(ResponseDto.ok());
	}
}
