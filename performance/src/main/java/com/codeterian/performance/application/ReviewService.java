package com.codeterian.performance.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.review.Review;
import com.codeterian.performance.infrastructure.persistence.PerformanceRepositoryImpl;
import com.codeterian.performance.infrastructure.persistence.ReviewRepositoryImpl;
import com.codeterian.performance.presentation.dto.request.ReviewAddRequestDto;
import com.codeterian.performance.presentation.dto.request.ReviewModifyRequestDto;
import com.codeterian.performance.presentation.dto.response.ReviewDetailsResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepositoryImpl reviewRepository;
	private final PerformanceRepositoryImpl performanceRepository;

	public void addReview(@Valid ReviewAddRequestDto dto) {
		// 공연정보랑 유저아이디로 리뷰를 이미 등록했는지 확인하는 검증 추가하기

		// 공연 존재 여부 확인
		Performance performance = performanceRepository.findByIdAndIsDeletedFalse(dto.performanceId()).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 공연입니다.")
		);

		Review newReview = Review.builder()
			.performance(performance)
			.title(dto.title())
			.description(dto.description())
			.rating(dto.rating())
			.build();

		reviewRepository.save(newReview);
	}

	public void modifyReview(UUID reviewId, ReviewModifyRequestDto dto) {
		// 리뷰 존재 여부 확인
		Review existingReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 리뷰입니다.")
		);

		Performance performance = performanceRepository.findByIdAndIsDeletedFalse(dto.performanceId()).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 공연입니다.")
		);

		// 일괄 업데이트 호출
		existingReview.update(
			dto.title(),
			dto.description(),
			dto.rating(),
			performance
		);

		reviewRepository.save(existingReview);

	}

	public ReviewDetailsResponseDto findReviewDetails(UUID reviewId) {
		// 리뷰 존재 여부 확인
		Review existingReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 리뷰입니다.")
		);

		return ReviewDetailsResponseDto.fromEntity(existingReview);
	}
}
