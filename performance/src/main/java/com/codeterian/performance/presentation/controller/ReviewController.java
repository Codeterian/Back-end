package com.codeterian.performance.presentation.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeterian.common.infrastructure.util.CurrentPassport;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.performance.application.ReviewService;
import com.codeterian.performance.presentation.dto.request.ReviewAddRequestDto;
import com.codeterian.performance.presentation.dto.request.ReviewModifyRequestDto;
import com.codeterian.performance.presentation.dto.response.ReviewAddResponseDto;
import com.codeterian.performance.presentation.dto.response.ReviewDetailsResponseDto;
import com.codeterian.performance.presentation.dto.response.ReviewModifyResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@RestController
@RequestMapping("/api/v1/performances/reviews")
@RequiredArgsConstructor
@Tag(name = "공연 리뷰 API")
public class ReviewController {

	private final ReviewService reviewService;

	@Operation(summary = "공연 리뷰 등록",description = "공연 리뷰 등록 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리뷰 등록 성공",
		content = {@Content(schema = @Schema(implementation = ReviewAddResponseDto.class))}),
		@ApiResponse(responseCode = "404",description = "존재하지 않는 공연입니다."),
		@ApiResponse(responseCode = "409",description = "이미 리뷰를 등록하셨습니다."),
		@ApiResponse(responseCode = "403",description ="고객 회원이 아닙니다.")
	})
	@PostMapping
	public ResponseEntity<ReviewAddResponseDto> reviewAdd(
		@Valid @RequestBody ReviewAddRequestDto dto,
		@Parameter(hidden = true)@CurrentPassport Passport passport){
		return ResponseEntity.ok().body(reviewService.addReview(dto,passport));
	}

	@Operation(summary = "공연 리뷰 수정",description = "공연 리뷰 수정 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리뷰 수정 성공",
			content = {@Content(schema = @Schema(implementation = ReviewModifyResponseDto.class))}),
		@ApiResponse(responseCode = "404",description = "존재하지 않는 공연입니다."),
		@ApiResponse(responseCode = "404",description = "존재하지 않는 리뷰입니다."),
		@ApiResponse(responseCode = "403",description = "리뷰 작성자만 수정할 수 있습니다."),
		@ApiResponse(responseCode = "403",description ="고객 회원이 아닙니다.")
	})
	@PutMapping("/{reviewId}")
	public ResponseEntity<ReviewModifyResponseDto> reviewModify(
		@PathVariable UUID reviewId,
		@Valid @RequestBody ReviewModifyRequestDto dto,
		@Parameter(hidden = true)@CurrentPassport Passport passport){
		return ResponseEntity.ok().body(reviewService.modifyReview(reviewId,dto,passport));
	}

	@Operation(summary = "공연 리뷰 삭제",description = "공연 리뷰 삭제 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
		@ApiResponse(responseCode = "404",description = "존재하지 않는 리뷰입니다."),
		@ApiResponse(responseCode = "403",description = "리뷰 작성자만 수정할 수 있습니다.")
	})
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<String> reviewRemove(
		@PathVariable UUID reviewId,
		@Parameter(hidden = true)@CurrentPassport Passport passport) {
		reviewService.removeReview(reviewId,passport);
		return ResponseEntity.ok().body("리뷰 삭제에 성공했습니다.");
	}

	@Operation(summary = "리뷰 단건 조회",description = "리뷰 단건 조회 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200",description = "리뷰 단건 조회 성공",
			content = {@Content(schema = @Schema(implementation = ReviewDetailsResponseDto.class))}),
		@ApiResponse(responseCode = "404",description = "존재하지 않는 리뷰입니다.")
	})
	@GetMapping("/{reviewId}")
	public ResponseEntity<ReviewDetailsResponseDto> reviewDetails(@PathVariable UUID reviewId){
		return ResponseEntity.ok(reviewService.findReviewDetails(reviewId));
	}
}
