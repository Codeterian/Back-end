package com.codeterian.performance.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.review.Review;
import com.codeterian.performance.infrastructure.persistence.PerformanceRepositoryImpl;
import com.codeterian.performance.infrastructure.persistence.ReviewRepositoryImpl;
import com.codeterian.performance.presentation.dto.request.ReviewAddDto;
import com.codeterian.performance.presentation.dto.request.ReviewModifyDto;
import com.codeterian.performance.presentation.dto.response.ReviewDetailsDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepositoryImpl reviewRepository;
	private final PerformanceRepositoryImpl performanceRepository;

	public void addReview(@Valid ReviewAddDto dto) {
		// 공연정보랑 유저아이디로 리뷰를 이미 등록했는지 확인하는 검증 추가하기

		// 공연 존재 여부 확인
		Performance performance = performanceRepository.findByIdAndIsDeletedFalse(dto.performanceId()).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 공연입니다.")
		);

		// 평점 확인
		if (dto.rating() <0 || dto.rating() > 6){
			throw new IllegalArgumentException("평점은 1~5점 사이만 가능합니다.");
		}

		Review newReview = Review.builder()
			.performance(performance)
			.title(dto.title())
			.description(dto.description())
			.rating(dto.rating())
			.build();

		reviewRepository.save(newReview);
	}

	public void modifyReview(UUID reviewId, ReviewModifyDto dto) {
		// 리뷰 존재 여부 확인
		Review existingReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 리뷰입니다.")
		);

		Performance performance = performanceRepository.findByIdAndIsDeletedFalse(dto.performanceId()).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 공연입니다.")
		);

		// 평점 유효성 체크
		if (dto.rating() < 1 || dto.rating() > 5) {
			throw new IllegalArgumentException("평점은 1~5점 사이만 가능합니다.");
		}

		// 일괄 업데이트 호출
		existingReview.update(
			dto.title(),
			dto.description(),
			dto.rating(),
			performance
		);

		reviewRepository.save(existingReview);

	}

	public ReviewDetailsDto findReviewDetails(UUID reviewId) {
		// 리뷰 존재 여부 확인
		Review existingReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 리뷰입니다.")
		);

		return new ReviewDetailsDto(
			reviewId,existingReview.getPerformance().getId(),
			existingReview.getTitle(),
			existingReview.getDescription(),
			existingReview.getRating()
		);
	}
}
