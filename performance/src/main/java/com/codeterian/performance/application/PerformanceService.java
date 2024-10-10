package com.codeterian.performance.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import com.codeterian.performance.domain.category.Category;
import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.performance.PerformanceDocument;
import com.codeterian.performance.domain.repository.PerformanceRepository;
import com.codeterian.performance.infrastructure.persistence.CategoryRepositoryImpl;
import com.codeterian.performance.presentation.dto.request.PerformanceAddRequestDto;
import com.codeterian.performance.presentation.dto.request.PerformanceModifyRequestDto;
import com.codeterian.performance.presentation.dto.response.PerformanceDetailsResponseDto;
import com.codeterian.performance.presentation.dto.response.PerformanceSearchResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final PerformanceRepository performanceRepository;
    private final CategoryRepositoryImpl categoryRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ElasticsearchOperations elasticsearchOperations;

    public void addPerformance(PerformanceAddRequestDto dto) {
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
        kafkaProducerService.sendPerformanceToKafka(savedperformance.getId());

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
        existedPerformance.updatePerformance(dto, existingcategory);

        performanceRepository.save(existedPerformance);

        // Kafka를 통해 Elasticsearch에 저장하도록 메시지 발행
        kafkaProducerService.sendPerformanceToKafka(existedPerformance.getId());
    }

    public PerformanceDetailsResponseDto findPerformanceDetails(UUID performanceId) {
        Performance performance = performanceRepository.findByIdAndIsDeletedFalse(performanceId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 공연입니다.")
        );
        return PerformanceDetailsResponseDto.fromEntity(performance);
    }

    public List<PerformanceSearchResponseDto> searchPerformance(String query,int pageNumber, int pageSize) {
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
}
