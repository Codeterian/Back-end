package com.codeterian.performance.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.codeterian.performance.domain.performance.PerformanceDocument;

import lombok.Builder;

@Builder
public record PerformanceSearchResponseDto(
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
	boolean isDeleted,
	String titleImage,
	List<String> images
) {

	public static PerformanceSearchResponseDto fromDocument(PerformanceDocument performanceDocument) {
		return PerformanceSearchResponseDto.builder()
			.id(performanceDocument.getId())
			.title(performanceDocument.getTitle())
			.description(performanceDocument.getDescription())
			.location(performanceDocument.getLocation())
			.startDate(performanceDocument.getStartDate())
			.endDate(performanceDocument.getEndDate())
			.bookingStartTime(performanceDocument.getBookingStartDate())
			.bookingEndTime(performanceDocument.getBookingEndDate())
			.duration(performanceDocument.getDuration())
			.ageRestriction(performanceDocument.getAgeRestriction())
			.status(performanceDocument.getStatus())
			.ticketStock(performanceDocument.getTicketStock())
			.categoryId(performanceDocument.getCategoryId())
			.isDeleted(performanceDocument.isDeleted())
			.titleImage(performanceDocument.getTitleImage())
			.images(performanceDocument.getImages())
			.build();
	}
}
