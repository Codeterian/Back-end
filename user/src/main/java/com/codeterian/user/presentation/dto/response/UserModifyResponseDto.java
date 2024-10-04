package com.codeterian.user.presentation.dto.response;

import com.codeterian.user.domain.model.User;

public record UserModifyResponseDto(
        Long id,
        String name,
        String email
) {
    public static UserModifyResponseDto fromEntity(User user) {
        return new UserModifyResponseDto(user.getId(), user.getName(), user.getEmail());
    }
}
