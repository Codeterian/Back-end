package com.codeterian.performance.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codeterian.performance.domain.performance.Performance;
import com.codeterian.performance.domain.review.Review;

public interface ReviewRepositoryImpl extends JpaRepository<Review, UUID> {

	Optional<Review> findByIdAndIsDeletedFalse(UUID id);

	Boolean existsByCreatedByAndPerformanceIdAndIsDeletedFalse(Long userId, UUID performanceId);
}
