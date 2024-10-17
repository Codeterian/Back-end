package com.codeterian.payment.application;

import org.springframework.stereotype.Service;

import com.codeterian.payment.domain.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;

}
