package com.codeterian.performance.presentation.dto.response;

import java.util.UUID;

import com.codeterian.performance.domain.category.Category;

import lombok.Builder;

@Builder
public record ParentCategoryAddResponseDto(
	UUID id,
	String name
){
	public static ParentCategoryAddResponseDto fromEntity(Category category) {
		return ParentCategoryAddResponseDto.builder()
			.id(category.getId())
			.name(category.getName())
			.build();
	}
}
