package com.codeterian.user.presentation.dto.response;

import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.user.domain.model.User;

public record UserFindWithoutPasswordResponseDto(
        Long id,
        String name,
        String email,
        UserRole userRole
) {

    public static UserFindWithoutPasswordResponseDto fromEntity(User user) {
        return new UserFindWithoutPasswordResponseDto(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
