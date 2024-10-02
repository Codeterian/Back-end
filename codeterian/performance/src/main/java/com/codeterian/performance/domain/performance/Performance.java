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
public class Performance  {

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

    // title 수정
    public void modifyTitle(String title) {
        this.title = title;
    }

    // 설명 수정
    public void modifyDescription(String description) {
        this.description = description;
    }

    // 위치 수정
    public void modifyLocation(String location) {
        this.location = location;
    }

    // 시작일 수정
    public void modifyStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    // 종료일 수정
    public void modifyEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    // 예약 시작일 수정
    public void modifyBookingStartDate(LocalDateTime bookingStartTime) {
        this.bookingStartDate = bookingStartTime;
    }

    // 에약 종료일 수정
    public void modifyBookingEndDate(LocalDateTime bookingEndTime) {
        this.bookingEndDate = bookingEndTime;
    }

    // 시간 수정
    public void modifyDuration(int duration) {
        this.duration = duration;
    }

    // 나이 제한 수정
    public void modifyAgeRestriction(String ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    // 공연 상태 수정
    public void modifyStatus(String status) {
        this.status = PerformanceStatus.valueOf(status);
    }

    // 티켓 수량 수정
    public void modifyTicketStock(int ticketStock) {
        this.ticketStock = ticketStock;
    }

    // 카테고리 수정
    public void modifyCategory(Category category) {
        this.category = category;
    }
}
