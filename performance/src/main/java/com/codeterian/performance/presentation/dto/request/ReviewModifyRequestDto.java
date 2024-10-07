package com.codeterian.performance.presentation.dto.request;

import java.util.UUID;

import org.hibernate.validator.constraints.Range;

public record ReviewModifyRequestDto(
	UUID performanceId,
	String title,
	String description,
	@Range(min = 1,max = 5) Integer rating
) {
}
