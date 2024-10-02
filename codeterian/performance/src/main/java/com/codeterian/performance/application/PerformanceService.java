package com.codeterian.performance.application;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeterian.performance.domain.category.Category;
import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.performance.PerformanceStatus;
import com.codeterian.performance.infrastructure.persistence.CategoryRepositoryImpl;
import com.codeterian.performance.infrastructure.persistence.PerformanceRepositoryImpl;
import com.codeterian.performance.presentation.dto.request.PerformanceAddRequestDto;
import com.codeterian.performance.presentation.dto.request.PerformanceModifyRequestDto;
import com.codeterian.performance.presentation.dto.response.PerformanceDetailsResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final PerformanceRepositoryImpl performanceRepository;
    private final CategoryRepositoryImpl categoryRepository;

    public void addPerformance(PerformanceAddRequestDto dto) {
        // 카테고리 존재 여부 확인
        Category category = categoryRepository.findByIdAndIsDeletedFalse(dto.categoryId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카테고리 입니다.")
        );

        // 공연 title 중복 확인
        if (performanceRepository.existsByTitleAndIsDeletedFalse(dto.title())){
            throw new IllegalArgumentException("중복되는 공연 입니다.");
        }

        Performance newPerformance = Performance.builder()
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

        performanceRepository.save(newPerformance);

    }

    @Transactional
    public void modifyPerformance(UUID performanceId, PerformanceModifyRequestDto dto) {
        Performance existedPerformance = performanceRepository.findByIdAndIsDeletedFalse(performanceId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 공연입니다.")
        );

        // title 수정
        if (dto.title() != null && !dto.title().equals(existedPerformance.getTitle())) {
            existedPerformance.modifyTitle(dto.title());
        }

        // 설명 수정
        if (dto.description() != null && !dto.description().equals(existedPerformance.getDescription())) {
            existedPerformance.modifyDescription(dto.description());
        }

        // 위치 수정
        if (dto.location() != null && !dto.location().equals(existedPerformance.getLocation())) {
            existedPerformance.modifyLocation(dto.location());
        }

        // 시작일 수정
        if (dto.startDate() != null && !Objects.equals(existedPerformance.getStartDate(), dto.startDate())) {
            existedPerformance.modifyStartDate(dto.startDate());
        }

        // 종료일 수정
        if (dto.endDate() != null && !Objects.equals(existedPerformance.getEndDate(), dto.endDate())) {
            existedPerformance.modifyEndDate(dto.endDate());
        }

        // 예약일 수정
        if (dto.bookingStartTime() != null && !Objects.equals(existedPerformance.getBookingStartDate(), dto.bookingStartTime())) {
            existedPerformance.modifyBookingStartDate(dto.bookingStartTime());
        }

        // 예약 종료일 수정
        if (dto.bookingEndTime() != null && !Objects.equals(existedPerformance.getBookingEndDate(), dto.bookingEndTime())) {
            existedPerformance.modifyBookingEndDate(dto.bookingEndTime());
        }

        // 시간 수정
        if (dto.duration() != null && !Objects.equals(existedPerformance.getDuration(), dto.duration())) {
            existedPerformance.modifyDuration(dto.duration());
        }

        // 나이 제한 수정
        if (dto.ageRestriction() != null && !Objects.equals(existedPerformance.getAgeRestriction(), dto.ageRestriction())) {
            existedPerformance.modifyAgeRestriction(dto.ageRestriction());
        }

        // 공연 상태 수정
        if (dto.status() != null && !dto.status().equals(existedPerformance.getStatus().name())) {
            existedPerformance.modifyStatus(dto.status());
        }

        // 티켓 수량 수정
        if (dto.ticketStock() != null && !Objects.equals(existedPerformance.getTicketStock(), dto.ticketStock())) {
            existedPerformance.modifyTicketStock(dto.ticketStock());
        }

        // 카테고리 수정
        if (dto.categoryId() != null && !Objects.equals(existedPerformance.getCategory().getId(), dto.categoryId())) {
            Category category = categoryRepository.findByIdAndIsDeletedFalse(dto.categoryId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
            );
            existedPerformance.modifyCategory(category);
        }

        performanceRepository.save(existedPerformance);
    }

    public PerformanceDetailsResponseDto findPerformanceDetails(UUID performanceId) {
        Performance performance = performanceRepository.findByIdAndIsDeletedFalse(performanceId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 공연입니다.")
        );
        return new PerformanceDetailsResponseDto(
            performance.getId(),
            performance.getTitle(),
            performance.getDescription(),
            performance.getLocation(),
            performance.getStartDate(),
            performance.getEndDate(),
            performance.getBookingStartDate(),
            performance.getBookingEndDate(),
            performance.getDuration(),
            performance.getAgeRestriction(),
            performance.getStatus().name(),
            performance.getTicketStock(),
            performance.getCategory().getId()
        );
    }
}
