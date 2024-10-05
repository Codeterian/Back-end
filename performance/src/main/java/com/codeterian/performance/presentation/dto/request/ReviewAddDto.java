package com.codeterian.performance.presentation.dto.request;

import java.util.UUID;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewAddDto(
	@NotNull(message = "공연Id는 필수 입력입니다.") UUID performanceId,
	@NotBlank(message = "리뷰 제목을 입력해주세요.") String title,
	@NotBlank(message = "리뷰 내용을 입력해주세요.") String description,
	@NotNull(message = "평점을 입력해 주세요") @Range(min = 1,max = 5) Integer rating
) {
}
