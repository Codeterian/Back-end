package com.codeterian.performance.presentation.dto.response;

import java.util.UUID;

import com.codeterian.performance.domain.category.Category;

import lombok.Builder;

@Builder
public record ChildCategoryAddResponseDto(
	UUID id,
	String name,
	UUID parentId
) {
	public static ChildCategoryAddResponseDto fromEntity(Category category) {
		return ChildCategoryAddResponseDto.builder()
			.id(category.getId())
			.name(category.getName())
			.parentId(category.getParent().getId())
			.build();
	}
}
