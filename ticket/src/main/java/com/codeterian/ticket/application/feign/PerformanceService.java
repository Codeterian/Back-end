package com.codeterian.ticket.application.feign;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PerformanceService {

    void decreaseTicketStock(UUID performanceID);

}
