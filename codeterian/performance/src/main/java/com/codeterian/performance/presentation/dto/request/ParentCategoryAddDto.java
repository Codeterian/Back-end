package com.codeterian.performance.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ParentCategoryAddDto(
    @NotBlank(message = "카테고리 이름은 필수입니다.") String name
) {

}
