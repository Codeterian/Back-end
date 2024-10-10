package com.codeterian.performance.domain.performance;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "performances")
public class PerformanceDocument {

	@Id
	private UUID id;

	@Field(type = FieldType.Text)
	private String title;

	@Field(type = FieldType.Text)
	private String description;

	@Field(type = FieldType.Text)
	private String location;

	@Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS||epoch_millis")
	private LocalDateTime startDate;

	@Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS||epoch_millis")
	private LocalDateTime endDate;

	@Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS||epoch_millis")
	private LocalDateTime bookingStartDate;

	@Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS||epoch_millis")
	private LocalDateTime bookingEndDate;

	@Field(type = FieldType.Integer)
	private int duration;

	@Field(type = FieldType.Text)
	private String ageRestriction;

	@Field(type = FieldType.Keyword)
	private String status;

	@Field(type = FieldType.Integer)
	private int ticketStock;

	@Field(type = FieldType.Text)
	private String username;

	@Field(type = FieldType.Keyword)
	private UUID categoryId;

	public static PerformanceDocument from(Performance performance) {
		return PerformanceDocument.builder()
			.id(performance.getId())
			.title(performance.getTitle())
			.description(performance.getDescription())
			.location(performance.getLocation())
			.startDate(performance.getStartDate())
			.endDate(performance.getEndDate())
			.bookingStartDate(performance.getBookingStartDate())
			.bookingEndDate(performance.getBookingEndDate())
			.duration(performance.getDuration())
			.ageRestriction(performance.getAgeRestriction())
			.status(performance.getStatus().name())
			.ticketStock(performance.getTicketStock())
			.username(performance.getUsername())
			.categoryId(performance.getCategory().getId())
			.build();

	}


}
