package com.codeterian.queue.application.feign.dto;

import java.util.UUID;

public record OrderAddResponseDto(

        UUID orderId,
        Long userId,
        Integer totalPrice
) {

}
