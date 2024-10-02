package com.codeterian.performance.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeterian.performance.domain.performance.Performance;

public interface PerformanceRepositoryImpl extends JpaRepository<Performance, UUID> {

    Boolean existsByTitleAndIsDeletedFalse(String title);

    Optional<Performance> findByIdAndIsDeletedFalse(UUID id);
}
