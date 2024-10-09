package com.codeterian.performance.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.codeterian.performance.domain.performance.Performance;

public interface PerformanceRepository {

	Performance save(Performance performance);

	Boolean existsByTitleAndIsDeletedFalse(String title);

	Optional<Performance> findByIdAndIsDeletedFalse(UUID id);

	Optional<Performance> findById(UUID id);
}
