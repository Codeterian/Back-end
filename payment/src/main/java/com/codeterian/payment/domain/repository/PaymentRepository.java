package com.codeterian.payment.domain.repository;

import com.codeterian.payment.domain.entity.payment.Payment;

public interface PaymentRepository {

	Payment save(Payment payment);
}
