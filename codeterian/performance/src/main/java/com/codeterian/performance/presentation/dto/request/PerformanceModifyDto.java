package com.codeterian.performance.presentation.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

public record PerformanceModifyDto(
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

}
