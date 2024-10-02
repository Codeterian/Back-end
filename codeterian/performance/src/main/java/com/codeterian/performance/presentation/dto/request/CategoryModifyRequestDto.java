package com.codeterian.performance.presentation.dto.request;

import java.util.UUID;

public record CategoryModifyRequestDto(
    String name,
    UUID parentId
) {

}
