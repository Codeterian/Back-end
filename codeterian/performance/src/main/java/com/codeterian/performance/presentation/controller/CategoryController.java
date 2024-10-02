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

import com.codeterian.performance.application.CategoryService;
import com.codeterian.performance.presentation.dto.request.CategoryModifyDto;
import com.codeterian.performance.presentation.dto.request.ChildCategoryAddDto;
import com.codeterian.performance.presentation.dto.request.ParentCategoryAddDto;
import com.codeterian.performance.presentation.dto.response.CategoryDetailsDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	// 부모 카테고리 등록
	@PostMapping("/parent")
	public ResponseEntity<?> parentCategoryAdd(@Valid @RequestBody ParentCategoryAddDto dto){
		categoryService.addParentCategory(dto);
		return ResponseEntity.ok().body("카테고리 등록에 성공했습니다.");
	}

	// 자식 카테고리 등록
	@PostMapping("/child")
	public ResponseEntity<?> childCategoryAdd(@Valid @RequestBody ChildCategoryAddDto dto){
		categoryService.addChildCategory(dto);
		return ResponseEntity.ok().body("카테고리 등록에 성공했습니다.");
	}

	// 카테고리 수정
	@PutMapping("/{categoryId}")
	public ResponseEntity<?> categoryModify(@PathVariable UUID categoryId, @Valid @RequestBody CategoryModifyDto dto){
		categoryService.modifyCategory(categoryId,dto);
		return ResponseEntity.ok().body("카테고리 수정에 성공했습니다.");
	}

	// // 카테고리 삭제
	// @DeleteMapping("/{categoryId}")
	// public ResponseEntity<?> CategoryRemove(@PathVariable UUID categoryId){
	// 	categoryService.removeCategory(categoryId);
	// 	return ResponseEntity.ok().body("카테고리 삭제에 성공했습니다.");
	// }

	// 카테고리 단건 조회
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDetailsDto> categoryDetails(@PathVariable UUID categoryId){
		return ResponseEntity.ok(categoryService.findCategoryDetails(categoryId));
	}

}