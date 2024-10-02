package com.codeterian.payment.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.codeterian.payment.domain.entity.payment.Payment;
import com.codeterian.payment.presentation.dto.PaymentDetailsResponseDto;

public interface PaymentRepository {

	Payment save(Payment payment);

	Optional<Payment> findById(UUID paymentId);

	List<Payment> findAll();

}
