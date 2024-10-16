package com.codeterian.queue.application.feign;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.queue.application.feign.dto.OrderAddRequestDto;
import com.codeterian.queue.application.feign.dto.OrderAddResponseDto;
import com.codeterian.queue.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "order",
        fallbackFactory = OrderFallBackFactory.class,
        configuration = FeignConfig.class)
public interface OrderFeignClient extends OrderService {

    @PostMapping("/api/v1/orders")
    ResponseDto<OrderAddResponseDto> orderAdd(OrderAddRequestDto requestDto);

}
