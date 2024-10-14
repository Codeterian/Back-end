package com.codeterian.ticket.application.feign;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class UserFallBackFactory implements FallbackFactory<UserFallBack> {

    @Override
    public UserFallBack create(Throwable cause) {
        if(cause instanceof FeignException) {
            log.error("catched exception", cause);
        }

        return null;
    }

}
