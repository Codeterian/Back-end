package com.codeterian.performance.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PerformanceDetailsDto (
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
){
}
