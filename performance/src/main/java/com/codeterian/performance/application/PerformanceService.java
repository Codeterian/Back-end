package com.codeterian.performance.application;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeterian.common.infrastructure.dto.performance.PerformanceDecreaseStockRequestDto;
import com.codeterian.performance.domain.category.Category;
import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.performance.PerformanceStatus;
import com.codeterian.performance.infrastructure.kafka.PerformanceKafkaProducer;
import com.codeterian.performance.infrastructure.persistence.CategoryRepositoryImpl;
import com.codeterian.performance.infrastructure.persistence.PerformanceRepositoryImpl;
import com.codeterian.performance.presentation.dto.request.PerformanceAddRequestDto;
import com.codeterian.performance.presentation.dto.request.PerformanceModifyRequestDto;
import com.codeterian.performance.presentation.dto.response.PerformanceDetailsResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerformanceService {

	private final PerformanceRepositoryImpl performanceRepository;
	private final CategoryRepositoryImpl categoryRepository;
	private final PerformanceKafkaProducer performanceKafkaProducer;

	public void addPerformance(PerformanceAddRequestDto dto) {
		// 카테고리 존재 여부 확인
		Category category = categoryRepository.findByIdAndIsDeletedFalse(dto.categoryId()).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 카테고리 입니다.")
		);

		// 공연 title 중복 확인
		if (performanceRepository.existsByTitleAndIsDeletedFalse(dto.title())) {
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
        return PerformanceDetailsResponseDto.fromEntity(performance);
    }

	@Transactional
	public void modifyStock(PerformanceDecreaseStockRequestDto performanceDecreaseStockRequestDto) throws
		JsonProcessingException {
		Performance performance = performanceRepository.findByIdAndIsDeletedFalse(
				performanceDecreaseStockRequestDto.performanceId())
			.orElseThrow(NoSuchElementException::new);

		//rollback logic
		if(performance.getTicketStock() < performanceDecreaseStockRequestDto.ticketAddRequestDtoList().size()){

		}
		performance.modifyStock(performanceDecreaseStockRequestDto.ticketAddRequestDtoList().size());

		performanceKafkaProducer.makeTicket(performanceDecreaseStockRequestDto.orderId(),
			performanceDecreaseStockRequestDto.userId(),
			performanceDecreaseStockRequestDto.ticketAddRequestDtoList());
	}
}
