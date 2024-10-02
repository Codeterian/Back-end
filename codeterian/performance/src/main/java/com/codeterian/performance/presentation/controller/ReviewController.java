package com.codeterian.performance.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeterian.performance.application.ReviewService;
import com.codeterian.performance.presentation.dto.request.ReviewAddDto;

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
}
