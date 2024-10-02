package com.codeterian.performance.domain.performance;

import java.time.LocalDateTime;
import java.util.UUID;

import com.codeterian.performance.domain.category.Category;

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
public class Performance {

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

    public void update(
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
        Category category
    ) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingStartDate = bookingStartTime;
        this.bookingEndDate = bookingEndTime;
        this.duration = duration;
        this.ageRestriction = ageRestriction;
        this.status = PerformanceStatus.valueOf(status); // 상태 값은 enum 처리
        this.ticketStock = ticketStock;
        this.category = category;
    }
}
