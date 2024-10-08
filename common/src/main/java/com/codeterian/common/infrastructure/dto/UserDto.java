package com.codeterian.common.infrastructure.dto;

import java.util.Collection;

public record UserDto(
        String userName,
        Collection<String> roles) {

}
