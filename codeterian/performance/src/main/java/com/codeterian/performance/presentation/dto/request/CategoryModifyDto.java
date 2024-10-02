package com.codeterian.performance.presentation.dto.request;

import java.util.UUID;

public record CategoryModifyDto(
    String name,
    UUID parentId
) {

}
