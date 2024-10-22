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
import com.codeterian.performance.application.CategoryService;
import com.codeterian.performance.presentation.dto.request.CategoryModifyRequestDto;
import com.codeterian.performance.presentation.dto.request.ChildCategoryAddRequestDto;
import com.codeterian.performance.presentation.dto.request.ParentCategoryAddRequestDto;
import com.codeterian.performance.presentation.dto.response.CategoryDetailsResponseDto;
import com.codeterian.performance.presentation.dto.response.CategoryModifyResponseDto;
import com.codeterian.performance.presentation.dto.response.ChildCategoryAddResponseDto;
import com.codeterian.performance.presentation.dto.response.ParentCategoryAddResponseDto;

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
@RequestMapping("/api/v1/performances/categories")
@RequiredArgsConstructor
@Tag(name = "공연 카테고리 API")
public class CategoryController {

	private final CategoryService categoryService;

	@Operation(summary = "부모 카테고리 등록" ,description = "부모 카테고리 등록 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200" , description = "부모 카테고리 등록 성공",
		content = {@Content(schema = @Schema(implementation = ParentCategoryAddResponseDto.class))}),
		@ApiResponse(responseCode = "403" ,description = "관리자 회원이 아닙니다."),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 카테고리입니다.")
	})
	@PostMapping("/parent")
	public ResponseEntity<ParentCategoryAddResponseDto> parentCategoryAdd(
		@Valid @RequestBody ParentCategoryAddRequestDto dto,
		@Parameter(hidden = true) @CurrentPassport Passport passport) throws IllegalAccessException {
		return ResponseEntity.ok().body(categoryService.addParentCategory(dto,passport));
	}

	@Operation(summary = "자식 카테고리 등록" , description = "자식 카테고리 등록 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200",description = "자식 카테고리 등록 성공",
		content = {@Content(schema = @Schema(implementation = ChildCategoryAddResponseDto.class))}),
		@ApiResponse(responseCode = "403",description = "관리자 회원이 아닙니다."),
		@ApiResponse(responseCode = "404",description = "상위 카테고리가 존재하지 않습니다.")
	})
	@PostMapping("/child")
	public ResponseEntity<ChildCategoryAddResponseDto> childCategoryAdd(
		@Valid @RequestBody ChildCategoryAddRequestDto dto,
		@Parameter(hidden = true) @CurrentPassport Passport passport){
		return ResponseEntity.ok().body(categoryService.addChildCategory(dto,passport));
	}

	@Operation(summary = "카테고리 수정", description = "카테고리 수정 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200",description = "카테고리 수정 성공",
		content = {@Content(schema = @Schema(implementation = CategoryModifyResponseDto.class))}),
		@ApiResponse(responseCode = "403",description = "관리자 회원이 아닙니다."),
		@ApiResponse(responseCode = "404",description = "존재하지 않는 카테고리입니다."),
		@ApiResponse(responseCode = "404",description = "이미 존재하는 카테고리입니다.")
	})
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryModifyResponseDto> categoryModify(
		@PathVariable UUID categoryId,
		@Valid @RequestBody CategoryModifyRequestDto dto,
		@Parameter(hidden = true) @CurrentPassport Passport passport){
		return ResponseEntity.ok().body(categoryService.modifyCategory(categoryId,dto,passport));
	}

	@Operation(summary = "카테고리 삭제" ,description = "카테고리 삭제 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200",description = "카테고리 삭제 성공"),
		@ApiResponse(responseCode = "403",description = "관리자 회원이 아닙니다."),
		@ApiResponse(responseCode = "404",description = "존재하지 않는 카테고리입니다.")
	})
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> CategoryRemove(
		@PathVariable UUID categoryId,
		@Parameter(hidden = true) @CurrentPassport Passport passport){
		categoryService.removeCategory(categoryId,passport);
		return ResponseEntity.ok().body("카테고리 삭제에 성공했습니다.");
	}

	@Operation(summary = "카테고리 단건 조회", description = "카테고리 단건 조회 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200",description = "카테고리 단건 조회",
		content = {@Content(schema = @Schema(implementation = CategoryDetailsResponseDto.class))}),
		@ApiResponse(responseCode = "403",description = "관리자 회원이 아닙니다."),
		@ApiResponse(responseCode = "404",description = "존재하지 않는 카테고리입니다.")
	})
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDetailsResponseDto> categoryDetails(
		@PathVariable UUID categoryId,
		@Parameter(hidden = true) @CurrentPassport Passport passport){
		return ResponseEntity.ok(categoryService.findCategoryDetails(categoryId,passport));
	}

}