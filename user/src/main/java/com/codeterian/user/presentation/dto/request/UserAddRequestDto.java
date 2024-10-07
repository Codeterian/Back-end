package com.codeterian.user.presentation.dto.request;

import com.codeterian.common.infrastructure.entity.UserRole;

public record UserAddRequestDto(
        String name,
        String password,
        String email,
        UserRole userRole

) {

}
