package com.codeterian.performance.application;

import org.springframework.stereotype.Service;

import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.review.Review;
import com.codeterian.performance.infrastructure.persistence.PerformanceRepositoryImpl;
import com.codeterian.performance.infrastructure.persistence.ReviewRepositoryImpl;
import com.codeterian.performance.presentation.dto.request.ReviewAddDto;

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

		Review newReview = Review.builder()
			.performance(performance)
			.title(dto.title())
			.description(dto.description())
			.rating(dto.rating())
			.build();

		reviewRepository.save(newReview);
	}
}
