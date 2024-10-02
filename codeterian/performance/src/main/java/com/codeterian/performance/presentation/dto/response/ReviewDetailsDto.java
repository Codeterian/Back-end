package com.codeterian.performance.presentation.dto.response;

import java.util.UUID;

public record ReviewDetailsDto(
	UUID reviewId,
	UUID performanceId,
	String title,
	String description,
	Integer rating
) {
}
