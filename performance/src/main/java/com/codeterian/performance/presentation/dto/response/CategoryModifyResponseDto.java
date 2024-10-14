package com.codeterian.performance.presentation.dto.response;

import java.util.UUID;

import com.codeterian.performance.domain.category.Category;

import lombok.Builder;

@Builder
public record CategoryModifyResponseDto(
	UUID id,
	String name,
	UUID parentId
) {

	public static CategoryModifyResponseDto valueOf(Category category) {
		if (category.getParent() == null) {
			return CategoryModifyResponseDto.builder()
				.id(category.getId())
				.name(category.getName())
				.build();
		}
		return CategoryModifyResponseDto.builder()
			.id(category.getId())
			.name(category.getName())
			.parentId(category.getParent().getId())
			.build();
	}
}
