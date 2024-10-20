package com.codeterian.performance.application;

import static com.codeterian.performance.exception.PerformanceErrorCode.*;
import static com.codeterian.performance.exception.ReviewErrorCode.*;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeterian.common.exception.RestApiException;
import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.review.Review;
import com.codeterian.performance.infrastructure.persistence.PerformanceRepositoryImpl;
import com.codeterian.performance.infrastructure.persistence.ReviewRepositoryImpl;
import com.codeterian.performance.presentation.dto.request.ReviewAddRequestDto;
import com.codeterian.performance.presentation.dto.request.ReviewModifyRequestDto;
import com.codeterian.performance.presentation.dto.response.ReviewAddResponseDto;
import com.codeterian.performance.presentation.dto.response.ReviewDetailsResponseDto;
import com.codeterian.performance.presentation.dto.response.ReviewModifyResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepositoryImpl reviewRepository;
	private final PerformanceRepositoryImpl performanceRepository;

	public ReviewAddResponseDto addReview(@Valid ReviewAddRequestDto dto, Passport passport) {
		validateCustomerRole(passport);

		Performance performance = performanceRepository.findByIdAndIsDeletedFalse(dto.performanceId()).orElseThrow(
			() -> new RestApiException(PERFORMANCE_NOT_FOUND)
		);

		if (reviewRepository.existsByCreatedByAndPerformanceIdAndIsDeletedFalse(passport.getUserId(), performance.getId())){
			throw new RestApiException(DUPLICATE_REVIEW);
		}

		Review newReview = Review.addReview(dto,passport.getUserId(),performance);

		Review savedReview = reviewRepository.save(newReview);

		return ReviewAddResponseDto.fromEntity(savedReview);
	}

	public ReviewModifyResponseDto modifyReview(UUID reviewId, ReviewModifyRequestDto dto,Passport passport) {
		validateCustomerRole(passport);

		Review existingReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId).orElseThrow(
			() -> new RestApiException(REVIEW_NOT_FOUND)
		);

		checkReviewEditPermission(passport, existingReview);

		Performance performance = performanceRepository.findByIdAndIsDeletedFalse(dto.performanceId()).orElseThrow(
			() -> new RestApiException(PERFORMANCE_NOT_FOUND)
		);

		existingReview.modify(dto ,performance,passport.getUserId());

		Review savedReview = reviewRepository.save(existingReview);

		return ReviewModifyResponseDto.fromEntity(savedReview);
	}

	public void removeReview(UUID reviewId, Passport passport) {
		Review review = reviewRepository.findByIdAndIsDeletedFalse(reviewId).orElseThrow(
			() -> new RestApiException(REVIEW_NOT_FOUND)
		);

		checkReviewEditPermission(passport, review);

		review.delete(passport.getUserId());

		reviewRepository.save(review);
	}

	public ReviewDetailsResponseDto findReviewDetails(UUID reviewId) {
		Review existingReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId).orElseThrow(
			() -> new RestApiException(REVIEW_NOT_FOUND)
		);

		return ReviewDetailsResponseDto.fromEntity(existingReview);
	}

	private static void validateCustomerRole(Passport passport) {
		UserRole userRole = passport.getUserRole();

		if (userRole != UserRole.CUSTOMER){
			throw new RestApiException(FORBIDDEN_CUSTOMER_ACCESS);
		}
	}

	private static void checkReviewEditPermission(Passport passport, Review existingReview) {
		if (!existingReview.getCreatedBy().equals(passport.getUserId())){
			if (passport.getUserRole() != UserRole.MANAGER && passport.getUserRole() != UserRole.MASTER){
				throw new RestApiException(UNAUTHORIZED_REVIEW_MODIFICATION);
			}
		}
	}

}
