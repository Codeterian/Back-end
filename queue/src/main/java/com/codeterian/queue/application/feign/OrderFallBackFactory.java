package com.codeterian.queue.application.feign;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class OrderFallBackFactory implements FallbackFactory<OrderFallback> {

    @Override
    public OrderFallback create(Throwable cause) {
        if(cause instanceof FeignException) {
            log.error("catched exception", cause);
        }

        return null;
    }

}
