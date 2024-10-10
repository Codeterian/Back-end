package com.codeterian.ticket.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name = "performance",
        fallback = PerformanceServiceFallback.class)
public interface PerformanceFeignClient extends PerformanceService{

    @PutMapping("/api/v1/performances/ticketStock/{performanceId}")
    void decreaseTicketStock(@PathVariable("performanceId") UUID performanceId);

}
