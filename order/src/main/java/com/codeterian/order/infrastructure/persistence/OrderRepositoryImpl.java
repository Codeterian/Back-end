package com.codeterian.order.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeterian.order.domain.entity.order.Orders;
import com.codeterian.order.domain.repository.OrderRepository;

@Repository
public interface OrderRepositoryImpl extends JpaRepository<Orders, UUID> , OrderRepository {

}
