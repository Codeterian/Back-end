package com.codeterian.performance.presentation.dto.response;

import java.util.UUID;

import com.codeterian.performance.domain.review.Review;

import lombok.Builder;

@Builder
public record ReviewAddResponseDto(
	UUID id,
	UUID performanceId,
	String title,
	String description,
	Integer rating
) {

	public static ReviewAddResponseDto fromEntity(Review review) {
		return ReviewAddResponseDto.builder()
			.id(review.getId())
			.performanceId(review.getPerformance().getId())
			.title(review.getTitle())
			.description(review.getDescription())
			.rating(review.getRating())
			.build();
	}
}
