package com.codeterian.performance.application;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codeterian.common.infrastructure.dto.performance.PerformanceDecreaseStockRequestDto;
import com.codeterian.performance.domain.category.Category;
import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.performance.PerformanceDocument;
import com.codeterian.performance.domain.repository.PerformanceRepository;
import com.codeterian.performance.infrastructure.kafka.PerformanceKafkaProducer;
import com.codeterian.performance.infrastructure.persistence.CategoryRepositoryImpl;
import com.codeterian.performance.presentation.dto.request.PerformanceAddRequestDto;
import com.codeterian.performance.presentation.dto.request.PerformanceModifyRequestDto;
import com.codeterian.performance.presentation.dto.response.PerformanceAddResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceDetailsResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceModifyResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceSearchResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final CategoryRepositoryImpl categoryRepository;
	private final PerformanceKafkaProducer performanceKafkaProducer;
    private final ElasticsearchOperations elasticsearchOperations;

    public PerformanceAddResponseDto addPerformance(PerformanceAddRequestDto dto) {
        // 카테고리 존재 여부 확인
        Category category = categoryRepository.findByIdAndIsDeletedFalse(dto.categoryId()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 카테고리 입니다.")
        );

        // 공연 title 중복 확인
        if (performanceRepository.existsByTitleAndIsDeletedFalse(dto.title())){
            throw new IllegalArgumentException("중복되는 공연 입니다.");
        }

        Performance newPerformance = Performance.addPerformance(dto, category);

        Performance savedperformance = performanceRepository.save(newPerformance);

        log.info("Performance DB 저장 성공");

        // Kafka를 통해 Elasticsearch에 저장하도록 메시지 발행
        performanceKafkaProducer.sendPerformanceToKafka(savedperformance.getId());

        return PerformanceAddResponseDto.fromEntity(savedperformance);
    }

    @Transactional
    public PerformanceModifyResponseDto modifyPerformance(UUID performanceId, PerformanceModifyRequestDto dto) {
        Performance existedPerformance = performanceRepository.findByIdAndIsDeletedFalse(performanceId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 공연입니다.")
        );

        Category existingcategory = categoryRepository.findByIdAndIsDeletedFalse(dto.categoryId()).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")
        );

        // 일괄 업데이트 메서드 호출
        existedPerformance.updatePerformance(dto, existingcategory);

        Performance savedPerformance = performanceRepository.save(existedPerformance);

        // Kafka를 통해 Elasticsearch에 저장하도록 메시지 발행
        performanceKafkaProducer.sendPerformanceToKafka(existedPerformance.getId());

        return PerformanceModifyResponseDto.fromEntity(savedPerformance);
    }

    public PerformanceDetailsResponseDto findPerformanceDetails(UUID performanceId) {
        Performance performance = performanceRepository.findByIdAndIsDeletedFalse(performanceId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 공연입니다.")
        );
        return PerformanceDetailsResponseDto.fromEntity(performance);
    }

    public List<PerformanceSearchResponseDto> searchPerformance(String query,int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("잘못된 pageNumber 요청입니다.");
        }

        if (pageSize <= 0) {
            throw new IllegalArgumentException("잘못된 pageSize 요청입니다.");
        }
        log.info("performance search query: {}", query);
        NativeQuery nativeQuery = NativeQuery.builder()
            .withQuery(q -> q
                .bool(b -> b
                    .should(s -> s.match(m -> m
                        .field("title")
                        .query(query)
                    ))
                    .should(s -> s.match(m -> m
                        .field("description")
                        .query(query)
                    ))
                    .should(s -> s.match(m -> m
                        .field("location")
                        .query(query)
                    ))
                )
            )
            .withPageable(PageRequest.of(pageNumber,pageSize))
            .build();

        SearchHits<PerformanceDocument> searchHits = elasticsearchOperations.search(nativeQuery, PerformanceDocument.class);
        return searchHits.getSearchHits().stream()
            .map(hit-> PerformanceSearchResponseDto.fromDocument(hit.getContent()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void removePerformance(UUID performanceId) {
        Performance performance = performanceRepository.findByIdAndIsDeletedFalse(performanceId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 공연입니다.")
        );

        // 나중에 userId 받아와서 수정하기
        performance.delete(1L);
        performanceRepository.save(performance);

        performanceKafkaProducer.sendPerformanceToKafka(performanceId);
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
