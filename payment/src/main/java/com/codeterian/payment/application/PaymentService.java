package com.codeterian.payment.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codeterian.payment.domain.entity.payment.Payment;
import com.codeterian.payment.domain.repository.PaymentRepository;
import com.codeterian.payment.presentation.dto.PaymentAddRequestDto;
import com.codeterian.payment.presentation.dto.PaymentAddResponseDto;
import com.codeterian.payment.presentation.dto.PaymentDetailsResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;

	@Transactional
	public PaymentAddResponseDto addPayment(PaymentAddRequestDto requestDto) {
		return PaymentAddResponseDto.fromEntity(paymentRepository.save(Payment.add(requestDto)));
	}

	@Transactional(readOnly = true)
	public PaymentDetailsResponseDto findPayment(UUID paymentId) {
		return PaymentDetailsResponseDto.fromEntity(findById(paymentId));
	}

	@Transactional(readOnly = true)
	public List<PaymentDetailsResponseDto> findPaymentList() {
		return paymentRepository.findAll().stream().map(PaymentDetailsResponseDto::fromEntity).toList();
	}


	@Transactional(readOnly = true)
	public Payment findById(UUID paymentId){
		return paymentRepository.findById(paymentId).orElseThrow(NoSuchElementException::new);
	}
}
