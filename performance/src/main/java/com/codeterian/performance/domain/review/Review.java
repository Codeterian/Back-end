package com.codeterian.performance.domain.review;

import java.util.UUID;

import com.codeterian.common.infrastructure.entity.BaseEntity;
import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.presentation.dto.request.ReviewAddRequestDto;
import com.codeterian.performance.presentation.dto.request.ReviewModifyRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "performance_id")
	private Performance performance;

	private String title;
	private String description;
	private int rating;

	public static Review addReview(ReviewAddRequestDto dto, Long id,Performance performance) {
		Review review = Review.builder()
			.performance(performance)
			.title(dto.title())
			.description(dto.description())
			.rating(dto.rating())
			.build();
		review.createBy(id);
		return review;
	}

	public void modify(ReviewModifyRequestDto dto, Performance performance,Long id) {
		this.title = dto.title();
		this.description = dto.description();
		this.rating = dto.rating();
		this.performance = performance;
		updateBy(id);
	}

}
