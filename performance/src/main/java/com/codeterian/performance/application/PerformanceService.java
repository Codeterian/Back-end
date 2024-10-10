package com.codeterian.performance.application;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeterian.common.infrastructure.dto.performance.PerformanceModifyStockRequestDto;
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

        Category existingcategory = categoryRepository.findByIdAndIsDeletedFalse(dto.categoryId()).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );

        // 일괄 업데이트 메서드 호출
        existedPerformance.update(
            dto.title(),
            dto.description(),
            dto.location(),
            dto.startDate(),
            dto.endDate(),
            dto.bookingStartTime(),
            dto.bookingEndTime(),
            dto.duration(),
            dto.ageRestriction(),
            dto.status(),
            dto.ticketStock(),
            existingcategory
        );

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

    @Transactional
    public void modifyStock(PerformanceModifyStockRequestDto performanceModifyStockRequestDto) {
        Performance performance = performanceRepository.findByIdAndIsDeletedFalse(performanceModifyStockRequestDto.performanceId())
            .orElseThrow(NoSuchElementException::new);
        performance.modifyStock(performanceModifyStockRequestDto.number());
    }
}
