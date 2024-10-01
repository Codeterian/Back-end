package com.codeterian.user.presentation.dto;

public record UserAddRequestDto(
        String name,
        String password,
        String email

) {

}
