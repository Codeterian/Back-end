package com.codeterian.performance.domain.performance;

import java.time.LocalDateTime;
import java.util.UUID;

import com.codeterian.common.infrastructure.entity.BaseEntity;
import com.codeterian.performance.domain.category.Category;
import com.codeterian.performance.presentation.dto.request.PerformanceAddRequestDto;
import com.codeterian.performance.presentation.dto.request.PerformanceModifyRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime bookingStartDate;
    private LocalDateTime bookingEndDate;
    private int duration;
    private String ageRestriction;
    @Enumerated(EnumType.STRING)
    private PerformanceStatus status;
    private int ticketStock;
    private String username;
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    public static Performance addPerformance(PerformanceAddRequestDto dto,Category category) {

        if (dto.startDate().isAfter(dto.endDate())) {
            throw new IllegalArgumentException("시작일은 종료일보다 이전이어야 합니다.");
        }

        return Performance.builder()
            .title(dto.title())
            .description(dto.description())
            .location(dto.location())
            .startDate(dto.startDate())
            .endDate(dto.endDate())
            .bookingStartDate(dto.bookingStartTime())
            .bookingEndDate(dto.bookingEndTime())
            .duration(dto.duration())
            .ageRestriction(dto.ageRestriction())
            .status(PerformanceStatus.valueOf(dto.status()))
            .ticketStock(dto.ticketStock())
            .category(category)
            .build();
    }

    public void updatePerformance(PerformanceModifyRequestDto dto, Category category) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작일은 종료일보다 이전이어야 합니다.");
        }

        this.title = dto.title();
        this.description = dto.description();
        this.location = dto.location();
        this.startDate = dto.startDate();
        this.endDate = dto.endDate();
        this.bookingStartDate = dto.bookingStartTime();
        this.bookingEndDate = dto.bookingEndTime();
        this.duration = dto.duration();
        this.ageRestriction = dto.ageRestriction();
        this.status = PerformanceStatus.valueOf(dto.status()); // 상태 값은 enum 처리
        this.ticketStock = dto.ticketStock();
        this.category = category;
    }
}
