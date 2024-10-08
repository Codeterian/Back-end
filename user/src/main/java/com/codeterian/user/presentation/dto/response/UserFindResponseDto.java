package com.codeterian.user.presentation.dto.response;

import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.user.domain.model.User;

public record UserFindResponseDto(
        Long id,
        String name,
        String email
) {

    public static UserFindResponseDto fromEntity(User user) {
        return new UserFindResponseDto(user.getId(), user.getName(), user.getEmail());
    }
}
