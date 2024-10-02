package com.codeterian.payment.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeterian.payment.application.PaymentService;
import com.codeterian.payment.presentation.dto.PaymentAddRequestDto;
import com.codeterian.payment.presentation.dto.PaymentAddResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;
	@PostMapping
	public ResponseEntity<PaymentAddResponseDto> paymentAdd(
		@RequestBody PaymentAddRequestDto requestDto
	){
		return ResponseEntity.ok(paymentService.addPayment(requestDto));
	}

}
