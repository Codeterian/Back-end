package com.codeterian.user.presentation.dto.request;

public record UserAddRequestDto(
        String name,
        String password,
        String email

) {

}
