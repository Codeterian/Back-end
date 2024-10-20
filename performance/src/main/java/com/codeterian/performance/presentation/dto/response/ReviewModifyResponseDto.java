package com.codeterian.performance.presentation.dto.response;

import java.util.UUID;

import com.codeterian.performance.domain.review.Review;

import lombok.Builder;

@Builder
public record ReviewModifyResponseDto(
	UUID id,
	UUID performanceId,
	String title,
	String description,
	Integer rating
) {
	public static ReviewModifyResponseDto fromEntity(Review review) {
		return ReviewModifyResponseDto.builder()
			.id(review.getId())
			.performanceId(review.getPerformance().getId())
			.title(review.getTitle())
			.description(review.getDescription())
			.rating(review.getRating())
			.build();
	}
}
