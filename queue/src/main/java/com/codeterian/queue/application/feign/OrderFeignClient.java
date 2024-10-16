package com.codeterian.queue.application.feign;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.queue.application.feign.dto.OrderAddRequestDto;
import com.codeterian.queue.application.feign.dto.OrderAddResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "order")
public interface OrderFeignClient {

    @PostMapping("/api/v1/orders")
    ResponseDto<OrderAddResponseDto> orderAdd(OrderAddRequestDto requestDto);

}
