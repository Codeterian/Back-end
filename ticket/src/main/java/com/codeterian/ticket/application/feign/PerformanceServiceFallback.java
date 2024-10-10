package com.codeterian.ticket.application.feign;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class PerformanceServiceFallback implements PerformanceFeignClient{

    private final Throwable cause;

    public PerformanceServiceFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public void decreaseTicketStock(UUID performanceId) {
        if (cause instanceof FeignException.NotFound) {
            log.error("Feign modifyPerformance error!!");
        } else {
            log.error("fallback error");
        }
    }

}
