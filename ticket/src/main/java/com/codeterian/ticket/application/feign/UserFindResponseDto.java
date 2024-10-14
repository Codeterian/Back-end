package com.codeterian.ticket.application.feign;

public record UserFindResponseDto(
        Long id,
        String name,
        String email
) {

}
