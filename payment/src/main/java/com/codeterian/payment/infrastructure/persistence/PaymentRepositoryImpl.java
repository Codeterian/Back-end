package com.codeterian.payment.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeterian.payment.domain.entity.payment.Payment;
import com.codeterian.payment.domain.repository.PaymentRepository;

@Repository
public interface PaymentRepositoryImpl extends JpaRepository<Payment, UUID> , PaymentRepository {
}
