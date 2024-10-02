package com.codeterian.performance.presentation.dto.request;

import java.util.UUID;

public record ReviewModifyDto(
	 UUID performanceId,
	 String title,
	 String description,
	 Integer rating
) {
}
