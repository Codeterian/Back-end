package com.codeterian.queue.application.feign;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.queue.application.feign.dto.OrderAddRequestDto;
import com.codeterian.queue.application.feign.dto.OrderAddResponseDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderFallback implements OrderFeignClient{

    private final Throwable cause;

    public OrderFallback(Throwable cause) {
        this.cause = cause;
    }


    @Override
    public ResponseDto<OrderAddResponseDto> orderAdd(OrderAddRequestDto requestDto) {

        if(cause instanceof FeignException.NotFound) {
            log.error("error!");
        }
        else {
            log.error("fallback error!");
        }

        return null;
    }

}
