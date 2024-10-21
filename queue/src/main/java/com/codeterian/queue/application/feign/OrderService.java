package com.codeterian.queue.application.feign;

import com.codeterian.common.infrastructure.dto.ResponseDto;
import com.codeterian.queue.application.feign.dto.OrderAddRequestDto;
import com.codeterian.queue.application.feign.dto.OrderAddResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    ResponseDto<OrderAddResponseDto> orderAdd(OrderAddRequestDto requestDto);

}
