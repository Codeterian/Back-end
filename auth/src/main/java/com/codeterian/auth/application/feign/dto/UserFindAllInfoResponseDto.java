package com.codeterian.auth.application.feign.dto;

import com.codeterian.common.infrastructure.entity.UserRole;

public record UserFindAllInfoResponseDto(
        Long id,
        String name,
        String email,
        String password,
        UserRole userRole
) {
}
