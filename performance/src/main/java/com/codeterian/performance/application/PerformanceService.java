package com.codeterian.performance.application;

import static com.codeterian.performance.infrastructure.exception.PerformanceErrorCode.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codeterian.common.exception.RestApiException;
import com.codeterian.common.infrastructure.dto.performance.PerformanceDecreaseStockRequestDto;
import com.codeterian.common.infrastructure.entity.UserRole;
import com.codeterian.common.infrastructure.util.Passport;
import com.codeterian.performance.domain.category.Category;
import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.performance.PerformanceDocument;
import com.codeterian.performance.domain.repository.PerformanceDocumentRepository;
import com.codeterian.performance.domain.repository.PerformanceRepository;
import com.codeterian.performance.infrastructure.exception.PerformanceErrorCode;
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
    private final S3Service s3Service;
    private final PerformanceDocumentRepository performanceDocumentRepository;

    @Transactional
    public PerformanceAddResponseDto addPerformance(PerformanceAddRequestDto dto, MultipartFile titleImage,
        List<MultipartFile> images, Passport passport) throws IOException {

        validateAdminRole(passport);

        Category category = categoryRepository.findByIdAndIsDeletedFalse(dto.categoryId()).orElseThrow(
                () -> new RestApiException(CATEGORY_NOT_FOUND)
        );

        if (performanceRepository.existsByTitleAndIsDeletedFalse(dto.title())){
            throw new RestApiException(DUPLICATE_PERFORMANCE_TITLE);
        }

        String titleImageUrl = s3Service.uploadFile(titleImage);

        List<String> imageUrls = images.stream()
            .map(file -> {
                try {
                    return s3Service.uploadFile(file);
                } catch (IOException e) {
                    throw new RestApiException(IMAGE_UPLOAD_FAILED);
                }
            })
            .toList();

        Performance newPerformance = Performance.addPerformance(dto, category,titleImageUrl,imageUrls,passport.getUserId());

        Performance savedperformance = performanceRepository.save(newPerformance);

        if (savedperformance == null || savedperformance.getId() == null) {
            throw new RestApiException(DATABASE_SAVE_FAILED);
        }

        performanceKafkaProducer.sendPerformanceToKafka(savedperformance.getId());

        return PerformanceAddResponseDto.fromEntity(savedperformance);
    }

    @Transactional
    public PerformanceModifyResponseDto modifyPerformance(UUID performanceId, PerformanceModifyRequestDto dto,
        MultipartFile newTitleImage, List<MultipartFile> newImages, Passport passport) throws IOException {

        validateAdminRole(passport);

        Performance existedPerformance = performanceRepository.findByIdAndIsDeletedFalse(performanceId).orElseThrow(
            () -> new RestApiException(PERFORMANCE_NOT_FOUND)
        );

        Category existingcategory = categoryRepository.findByIdAndIsDeletedFalse(dto.categoryId()).orElseThrow(
            () -> new RestApiException(CATEGORY_NOT_FOUND)
        );

        String titleImageUrl = existedPerformance.getPerformanceImage().getTitleImage();
        if (newTitleImage != null && !newTitleImage.isEmpty()) {
            titleImageUrl = s3Service.uploadFile(newTitleImage);
        }

        List<String> imageUrls = existedPerformance.getPerformanceImage().getImages();
        if (newImages != null && !newImages.isEmpty()) {
            imageUrls = newImages.stream()
                .map(file-> {
                    try {
                        return s3Service.uploadFile(file);
                    } catch (IOException e) {
                        throw new RestApiException(IMAGE_UPLOAD_FAILED);
                    }
                })
                .toList();
        }

        existedPerformance.updatePerformance(dto, existingcategory,titleImageUrl,imageUrls,passport.getUserId());

        Performance savedPerformance = performanceRepository.save(existedPerformance);

        performanceKafkaProducer.sendPerformanceToKafka(existedPerformance.getId());

        return PerformanceModifyResponseDto.fromEntity(savedPerformance);
    }

    public PerformanceDetailsResponseDto findPerformanceDetails(UUID performanceId) {
        Performance performance = performanceRepository.findByIdAndIsDeletedFalse(performanceId).orElseThrow(
            () -> new RestApiException(PERFORMANCE_NOT_FOUND)
        );
        return PerformanceDetailsResponseDto.fromEntity(performance);
    }

    public List<PerformanceSearchResponseDto> searchPerformance(String query, int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            throw new RestApiException(INVALID_PAGE_NUMBER);
        }

        if (pageSize <= 0) {
            throw new RestApiException(INVALID_PAGE_SIZE);
        }

        NativeQuery nativeQuery = NativeQuery.builder()
            .withQuery(q -> q
                .bool(b -> b
                    .must(m -> m
                        .multiMatch(m1 -> m1
                            .query(query)
                            .fields(Arrays.asList("title^2", "description", "location"))
                        )
                    )
                    .filter(f -> f
                        .bool(bf -> bf
                            .must(t -> t
                                .term(t1 -> t1
                                    .field("isDeleted")
                                    .value(false)
                                )
                            )
                        )
                    )
                )
            )
            .withPageable(PageRequest.of(pageNumber, pageSize))
            .build();

        SearchHits<PerformanceDocument> searchHits = elasticsearchOperations.search(nativeQuery, PerformanceDocument.class);
        return searchHits.getSearchHits().stream()
            .map(hit -> PerformanceSearchResponseDto.fromDocument(hit.getContent()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void removePerformance(UUID performanceId, Passport passport) {
        validateAdminRole(passport);

        Performance performance = performanceRepository.findByIdAndIsDeletedFalse(performanceId).orElseThrow(
            () -> new RestApiException(PERFORMANCE_NOT_FOUND)
        );

        performance.delete(passport.getUserId());

        performanceRepository.save(performance);

        performanceKafkaProducer.sendPerformanceToKafka(performance.getId());
    }

  @Transactional
	public void modifyStock(PerformanceDecreaseStockRequestDto performanceDecreaseStockRequestDto) throws
        JsonProcessingException {
		Performance performance = performanceRepository.findByIdAndIsDeletedFalse(
				performanceDecreaseStockRequestDto.performanceId())
			.orElseThrow(NoSuchElementException::new);

		// 재고가 없을 시에 -> roll back
		if(performance.getTicketStock() < performanceDecreaseStockRequestDto.ticketAddRequestDtoList().size()){
            performanceKafkaProducer.stockIsEmpty(performanceDecreaseStockRequestDto.orderId());
		}
        else{
            performance.modifyStock(performanceDecreaseStockRequestDto.ticketAddRequestDtoList().size());
            performanceKafkaProducer.makeTicket(performanceDecreaseStockRequestDto.orderId(),
                performanceDecreaseStockRequestDto.userId(),
                performanceDecreaseStockRequestDto.ticketAddRequestDtoList());
        }
	}

    private static void validateAdminRole(Passport passport) {
        UserRole userRole = passport.getUserRole();

        if (userRole == UserRole.CUSTOMER){
            throw new RestApiException(FORBIDDEN_ADMIN_ACCESS);
        }
    }

    // 엘라스틱 서치에 더미데이터 저장
    public void migratePerformancesToElasticsearch() {
        List<Performance> performances = performanceRepository.findAll();

        for (Performance performance : performances) {
            PerformanceDocument document = PerformanceDocument.from(performance);
            performanceDocumentRepository.save(document);
        }
    }
}
