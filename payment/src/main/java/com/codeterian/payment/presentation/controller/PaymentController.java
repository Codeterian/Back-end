package com.codeterian.payment.presentation.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.common.infrastructure.dto.payment.PaymentAfterValidateRequestDto;
import com.codeterian.payment.application.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;


	@PostMapping("/after/validate")
	public ResponseEntity<ResponseDto<Void>> paymentAfterValidate(
		@RequestBody PaymentAfterValidateRequestDto paymentValidateRequestDto
	) throws IamportResponseException, IOException {
		paymentService.paymentAfterValidate(paymentValidateRequestDto);
		return ResponseEntity.ok(ResponseDto.ok());
	}

}
