package com.codeterian.auth.application.feign.dto;

public record UserFindResponseDto(
        Long id,
        String name,
        String email
) {
}
