package com.codeterian.performance.presentation.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeterian.performance.application.ReviewService;
import com.codeterian.performance.presentation.dto.request.ReviewAddDto;
import com.codeterian.performance.presentation.dto.request.ReviewModifyDto;
import com.codeterian.performance.presentation.dto.response.ReviewDetailsDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<?> reviewAdd(@Valid @RequestBody ReviewAddDto dto){
		reviewService.addReview(dto);
		return ResponseEntity.ok().body("리뷰 등록에 성공했습니다.");
	}

	@PutMapping("/{reviewId}")
	public ResponseEntity<?> reviewModify(@PathVariable UUID reviewId, @Valid @RequestBody ReviewModifyDto dto){
		reviewService.modifyReview(reviewId,dto);
		return ResponseEntity.ok().body("리뷰 수정에 성공했습니다.");
	}

	@GetMapping("/{reviewId}")
	public ResponseEntity<ReviewDetailsDto> reviewDetails(@PathVariable UUID reviewId){
		return ResponseEntity.ok(reviewService.findReviewDetails(reviewId));
	}
}
