package com.codeterian.performance.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ChildCategoryAddDto(
    @NotBlank(message = "카테고리 이름은 필수입니다.") String name,
    @NotNull(message = "상위 카테고리를 입력해주세요.") UUID parentId
) {

}
