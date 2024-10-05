package com.codeterian.performance.presentation.dto.response;

import java.util.UUID;

import com.codeterian.performance.domain.review.Review;

import lombok.Builder;

@Builder
public record ReviewDetailsDto(
	UUID reviewId,
	UUID performanceId,
	String title,
	String description,
	Integer rating
) {

	public static ReviewDetailsDto fromEntity(Review review) {
		return ReviewDetailsDto.builder()
			.reviewId(review.getId())
			.performanceId(review.getPerformance().getId())
			.title(review.getTitle())
			.description(review.getDescription())
			.rating(review.getRating())
			.build();
	}
}
