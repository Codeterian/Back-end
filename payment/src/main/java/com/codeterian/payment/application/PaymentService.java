package com.codeterian.payment.application;

import org.springframework.stereotype.Service;

import com.codeterian.payment.domain.entity.payment.Payment;
import com.codeterian.payment.domain.repository.PaymentRepository;
import com.codeterian.payment.presentation.dto.PaymentAddRequestDto;
import com.codeterian.payment.presentation.dto.PaymentAddResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;

	public PaymentAddResponseDto addPayment(PaymentAddRequestDto requestDto) {
		return PaymentAddResponseDto.fromEntity(paymentRepository.save(Payment.add(requestDto)));
	}


}
