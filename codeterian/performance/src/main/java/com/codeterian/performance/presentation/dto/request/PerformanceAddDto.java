package com.codeterian.performance.presentation.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

public record PerformanceAddDto (
     String title,
     String description,
     String location,
     LocalDateTime startDate,
     LocalDateTime endDate,
     LocalDateTime bookingStartTime,
     LocalDateTime bookingEndTime,
     int duration,
     String ageRestriction,
     String status,
     int ticketStock,
     UUID categoryId
){

}
