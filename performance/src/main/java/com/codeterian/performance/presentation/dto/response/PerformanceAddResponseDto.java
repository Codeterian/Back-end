package com.codeterian.performance.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.codeterian.performance.domain.performance.Performance;

import lombok.Builder;

@Builder
public record PerformanceAddResponseDto(
	UUID id,
	String title,
	String description,
	String location,
	LocalDateTime startDate,
	LocalDateTime endDate,
	LocalDateTime bookingStartTime,
	LocalDateTime bookingEndTime,
	Integer duration,
	String ageRestriction,
	String status,
	Integer ticketStock,
	UUID categoryId,
	String titleImageUrl,
	List<String> imagesUrl
) {

	public static PerformanceAddResponseDto fromEntity(Performance performance) {
		return PerformanceAddResponseDto.builder()
			.id(performance.getId())
			.title(performance.getTitle())
			.description(performance.getDescription())
			.location(performance.getLocation())
			.startDate(performance.getStartDate())
			.endDate(performance.getEndDate())
			.bookingStartTime(performance.getBookingStartDate())
			.bookingEndTime(performance.getBookingEndDate())
			.duration(performance.getDuration())
			.ageRestriction(performance.getAgeRestriction())
			.status(performance.getStatus().name())
			.ticketStock(performance.getTicketStock())
			.categoryId(performance.getCategory().getId())
			.titleImageUrl(performance.getTitleImage().getImageUrl())
			.imagesUrl(performance.getImages().stream()
				.map(image -> image.getImageUrl()) // 각 이미지의 URL 추출
				.collect(Collectors.toList())) // List<String>으로 변환
			.build();
	}
}
