package com.codeterian.user.presentation.dto.response;

import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.user.domain.model.User;

public record UserFindAllInfoResponseDto(
        Long id,
        String name,
        String email,
        String password,
        UserRole userRole
) {

    public static UserFindAllInfoResponseDto fromEntity(User user) {
        return new UserFindAllInfoResponseDto(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
