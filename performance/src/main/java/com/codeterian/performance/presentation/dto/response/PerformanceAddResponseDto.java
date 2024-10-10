package com.codeterian.performance.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

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
	UUID categoryId
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
			.build();
	}
}
