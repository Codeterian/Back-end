package com.codeterian.payment.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.payment.application.PaymentService;
import com.codeterian.common.infrastructure.dto.payment.PaymentAddRequestDto;
import com.codeterian.payment.presentation.dto.PaymentDetailsResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;
	@PostMapping
	public ResponseDto paymentAdd(
		@RequestBody PaymentAddRequestDto requestDto
	){
		paymentService.addPayment(requestDto);
		return ResponseDto.ok();
	}

	@GetMapping("/{paymentId}")
	public ResponseEntity<PaymentDetailsResponseDto> paymentDetails(
		@PathVariable UUID paymentId
	){
		return ResponseEntity.ok(paymentService.findPayment(paymentId));
	}

	@GetMapping
	public ResponseEntity<List<PaymentDetailsResponseDto>> paymentList(){
		return ResponseEntity.ok(paymentService.findPaymentList());
	}

}
